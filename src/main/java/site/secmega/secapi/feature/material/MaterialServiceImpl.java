package site.secmega.secapi.feature.material;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.base.MaterialStatus;
import site.secmega.secapi.base.TransactionType;
import site.secmega.secapi.domain.Material;
import site.secmega.secapi.domain.MaterialDetail;
import site.secmega.secapi.domain.User;
import site.secmega.secapi.feature.file.FileService;
import site.secmega.secapi.feature.material.dto.*;
import site.secmega.secapi.feature.user.UserRepository;
import site.secmega.secapi.mapper.MaterialMapper;
import site.secmega.secapi.util.AuthUtil;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService{

    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;
    private final UserRepository userRepository;
    private final MaterialDetailRepository materialDetailRepository;
    private final AuthUtil authUtil;

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
                    .user(detail.getUser().getFirstName())
                    .materialName(detail.getMaterial().getName())
                    .qtyBalance(detail.getQtyBalance())
                    .qtyInput(detail.getQuantity())
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
