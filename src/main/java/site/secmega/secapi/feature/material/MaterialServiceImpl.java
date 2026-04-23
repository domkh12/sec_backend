package site.secmega.secapi.feature.material;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.base.MaterialStatus;
import site.secmega.secapi.base.TransactionType;
import site.secmega.secapi.domain.Material;
import site.secmega.secapi.domain.MaterialDetail;
import site.secmega.secapi.domain.User;
import site.secmega.secapi.feature.material.dto.*;
import site.secmega.secapi.feature.report.GenerateReportService;
import site.secmega.secapi.feature.user.UserRepository;
import site.secmega.secapi.mapper.MaterialMapper;
import site.secmega.secapi.util.AuthUtil;
import site.secmega.secapi.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService{

    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;
    private final UserRepository userRepository;
    private final MaterialDetailRepository materialDetailRepository;
    private final AuthUtil authUtil;
    private final GenerateReportService generateReportService;

    @Value("${materialExcel.template.path}")
    String excelTemplatePath;

    @Value("${materialStockInExcel.template.path}")
    String stockInExcelTemplatePath;

    @Value("${materialStockOutExcel.template.path}")
    String stockOutExcelTemplatePath;

    @Override
    public ResponseEntity<InputStreamResource> getReportStockOut() throws IOException {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<MaterialDetail> materialDetails = materialDetailRepository.findByTypeOrderByIdDesc(TransactionType.INVENTORY_OUT, sort);

        File file = generateReportService.generateExcelReport(materialDetails, stockOutExcelTemplatePath);
        HttpHeaders headers = Util.getHttpHeaders("MaterialDetail", file, "xlsx", MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

        return new ResponseEntity<>(new InputStreamResource(new FileInputStream(file)), headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<InputStreamResource> getReportStockIn() throws IOException {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<MaterialDetail> materialDetails = materialDetailRepository.findByTypeOrderByIdDesc(TransactionType.INVENTORY_IN, sort);

        File file = generateReportService.generateExcelReport(materialDetails, stockInExcelTemplatePath);
        HttpHeaders headers = Util.getHttpHeaders("MaterialDetail", file, "xlsx", MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

        return new ResponseEntity<>(new InputStreamResource(new FileInputStream(file)), headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<InputStreamResource> getReportMaterial() throws IOException {

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<Material> materials = materialRepository.findAll(sort);

        File file = generateReportService.generateExcelReport(materials, excelTemplatePath);
        HttpHeaders headers = Util.getHttpHeaders("Material", file, "xlsx", MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

        return new ResponseEntity<>(new InputStreamResource(new FileInputStream(file)), headers, HttpStatus.OK);
    }

    @Override
    public MaterialResponse updateMaterial(Long id, MaterialRequest materialRequest) {
        Material material = materialRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material not found")
        );

        if (materialRepository.existsByCodeAndIdNotAndDeletedAtNull(materialRequest.code(), id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Material code already exist");
        }

        materialMapper.updateFromMaterialRequest(materialRequest, material);
        material.setUpdatedAt(LocalDateTime.now());
        Material updatedMaterial = materialRepository.save(material);
        return materialMapper.toMaterialResponse(updatedMaterial);
    }

    @Override
    public void deleteMaterial(Long id) {
        Material material = materialRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material not found")
        );
        material.setDeletedAt(LocalDateTime.now());
        materialRepository.save(material);
    }

    @Override
    public StockOutResponse stockOut(StockOutRequest stockOutRequest) {
        User user = userRepository.findById(stockOutRequest.requesterId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        Material material = materialRepository.findById(stockOutRequest.materialId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material not found")
        );
        if (material.getBalance() < stockOutRequest.qtyOutput()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Material balance is not enough");
        }

        MaterialDetail materialDetail = MaterialDetail.builder()
                .quantity(stockOutRequest.qtyOutput())
                .transactionDate(stockOutRequest.dateOutput())
                .material(material)
                .type(TransactionType.INVENTORY_OUT)
                .qtyBalance(material.getBalance() - stockOutRequest.qtyOutput())
                .user(user)
                .build();
        material.setBalance(material.getBalance() - stockOutRequest.qtyOutput());
        materialDetailRepository.save(materialDetail);
        materialRepository.save(material);

        return StockOutResponse.builder()
                .id(materialDetail.getId())
                .materialName(material.getName())
                .qtyBalance(materialDetail.getQtyBalance())
                .qtyOutput(materialDetail.getQuantity())
                .dateOutput(materialDetail.getTransactionDate())
                .requester(user.getNameEn())
                .build();
    }

    @Override
    public Page<StockOutResponse> getStockOut(Long id, StockOutFilterRequest stockOutFilterRequest) {
        if (stockOutFilterRequest.pageNo() <= 0 || stockOutFilterRequest.pageSize() <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no and Page size must be greater than 0");
        }

        Specification<MaterialDetail> spec = Specification.where((root, query, cb) -> cb.conjunction());

        spec = spec.and((root, query, cb) -> cb.equal(root.get("material").get("id"), id));
        spec = spec.and((root, query, cb) -> cb.equal(root.get("type"), TransactionType.INVENTORY_OUT));
        if (stockOutFilterRequest.search() != null){
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("material").get("name")), "%" + stockOutFilterRequest.search().toLowerCase() + "%")
            );
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(stockOutFilterRequest.pageNo() - 1, stockOutFilterRequest.pageSize(), sort);
        Page<MaterialDetail> materialDetails = materialDetailRepository.findAll(spec, pageRequest);

        return materialDetails.map(detail -> StockOutResponse.builder()
                .id(detail.getId())
                .requester(detail.getUser().getNameEn())
                .materialName(detail.getMaterial().getName())
                .qtyBalance(detail.getQtyBalance())
                .qtyOutput(detail.getQuantity())
                .unit(detail.getMaterial().getUnit())
                .dateOutput(detail.getTransactionDate())
                .build());
    }

    @Override
    public MaterialStatResponse getMaterialStat() {
        long totalMaterial = materialRepository.count();
        long totalLowStock = materialRepository.countByStatus(MaterialStatus.LOW_STOCK);
        long totalOutOfStock = materialRepository.countByStatus(MaterialStatus.OUT_OF_STOCK);
        return MaterialStatResponse.builder()
                .totalMaterial(totalMaterial)
                .totalLowStock(totalLowStock)
                .totalOutOfStock(totalOutOfStock)
                .build();
    }

    @Override
    public Page<StockInResponse> getStockIn(Long id, StockInFilterRequest stockInFilterRequest) {
        if (stockInFilterRequest.pageNo() <= 0 || stockInFilterRequest.pageSize() <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no and Page size must be greater than 0");
        }

        Specification<MaterialDetail> spec = Specification.where((root, query, cb) -> cb.conjunction());
        spec = spec.and((root, query, cb) -> cb.equal(root.get("material").get("id"), id));
        spec = spec.and((root, query, cb) -> cb.equal(root.get("type"), TransactionType.INVENTORY_IN));

        if (stockInFilterRequest.search() != null){
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("material").get("name")), "%" + stockInFilterRequest.search().toLowerCase() + "%")
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(stockInFilterRequest.pageNo() - 1, stockInFilterRequest.pageSize(), sort);
        Page<MaterialDetail> materialDetails = materialDetailRepository.findAll(spec, pageRequest);

        return materialDetails.map(detail -> {
            return StockInResponse.builder()
                    .id(detail.getId())
                    .user(detail.getUser().getNameEn())
                    .materialName(detail.getMaterial().getName())
                    .qtyBalance(detail.getQtyBalance())
                    .qtyInput(detail.getQuantity())
                    .unit(detail.getMaterial().getUnit())
                    .dateInput(detail.getTransactionDate())
                    .build();
        });
    }

    @Transactional
    @Override
    public StockInResponse stockIn(StockInRequest stockInRequest) {
        Long userId = authUtil.loggedUserId();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        Material material = materialRepository.findById(stockInRequest.materialId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material not found")
        );
        MaterialDetail materialDetail = MaterialDetail.builder()
                .quantity(stockInRequest.qtyInput())
                .transactionDate(stockInRequest.dateInput())
                .material(material)
                .type(TransactionType.INVENTORY_IN)
                .qtyBalance(material.getBalance() + stockInRequest.qtyInput())
                .user(user)
                .build();
        material.setBalance(material.getBalance() + stockInRequest.qtyInput());
        MaterialDetail savedMaterialDetail = materialDetailRepository.save(materialDetail);
        materialRepository.save(material);
        return StockInResponse.builder()
                .id(savedMaterialDetail.getId())
                .materialName(material.getName())
                .qtyBalance(savedMaterialDetail.getQtyBalance())
                .qtyInput(savedMaterialDetail.getQuantity())
                .build();
    }

    @Override
    public Page<MaterialResponse> findAll(MaterialFilterRequest materialFilterRequest) {

        if (materialFilterRequest.pageNo() <= 0 || materialFilterRequest.pageSize() <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no and Page size must be greater than 0");
        }

        Specification<Material> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (materialFilterRequest.search() != null){
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("code")), "%" + materialFilterRequest.search().toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("name")), "%" + materialFilterRequest.search().toLowerCase() + "%")
                    ));
        }

        if (materialFilterRequest.status() != null){
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), materialFilterRequest.status()));
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(materialFilterRequest.pageNo() - 1, materialFilterRequest.pageSize(), sort);
        Page<Material> materials = materialRepository.findAll(spec, pageRequest);

        return materials.map(material -> MaterialResponse.builder()
                .id(material.getId())
                .code(material.getCode())
                .name(material.getName())
                .balance(material.getBalance())
                .status(material.getStatus())
                .unit(material.getUnit())
                .image(material.getImage())
                .totalInput(material.getMaterialDetails().stream().filter(detail -> detail.getType() == TransactionType.INVENTORY_IN).mapToDouble(MaterialDetail::getQuantity).sum())
                .totalOutput(material.getMaterialDetails().stream().filter(detail -> detail.getType() == TransactionType.INVENTORY_OUT).mapToDouble(MaterialDetail::getQuantity).sum())
                .build());
    }

    @Override
    public MaterialResponse createMaterial(MaterialRequest materialRequest) throws IOException {

        if (materialRepository.existsByCodeAndDeletedAtNull(materialRequest.code())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Material code already exist");
        }

        if (materialRepository.existsByNameAndDeletedAtNull(materialRequest.name())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Material name already exist");
        }

        Material material = materialMapper.fromMaterialRequest(materialRequest);
        material.setStatus(MaterialStatus.OUT_OF_STOCK);
        material.setBalance(0.0);
        Material savedMaterial = materialRepository.save(material);

        return materialMapper.toMaterialResponse(savedMaterial);
    }
}
