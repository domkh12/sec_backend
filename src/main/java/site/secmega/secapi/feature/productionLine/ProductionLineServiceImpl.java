package site.secmega.secapi.feature.productionLine;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.base.ProductionLineStatus;
import site.secmega.secapi.domain.Department;
import site.secmega.secapi.domain.ProductionLine;
import site.secmega.secapi.feature.department.DepartmentRepository;
import site.secmega.secapi.feature.productionLine.dto.*;
import site.secmega.secapi.mapper.ProductionLineMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductionLineServiceImpl implements ProductionLineService{

    private final ProductionLineRepository productionLineRepository;
    private final ProductionLineMapper productionLineMapper;
    private final DepartmentRepository departmentRepository;


    @Override
    public List<ProductionLineLookupResponse> getProductionLineByDeptNo(Integer processNo) {
        Sort sort = Sort.by(Sort.Direction.ASC, "line");
        List<ProductionLine> productionLines = productionLineRepository.findByDepartment_ProcessNoOrderByLineAsc(processNo, sort);
        return productionLines.stream().map(productionLineMapper::toProductionLineLookupResponse).toList();
    }

    @Override
    public List<ProductionLineLookupResponse> getProductionLineByDept(Long id) {
        Sort sort = Sort.by(Sort.Direction.ASC, "line");
        List<ProductionLine> productionLines = productionLineRepository.findByDepartment_IdOrderByLineAsc(id, sort);
        return productionLines.stream().map(productionLineMapper::toProductionLineLookupResponse).toList();
    }

    @Override
    public List<ProductionLineLookupResponse> getProductionLineLookup() {
        Sort sort = Sort.by(Sort.Direction.ASC, "line");
        List<ProductionLine> productionLines = productionLineRepository.findAll(sort);

        return productionLines.stream().map(productionLineMapper::toProductionLineLookupResponse).toList();
    }

    @Override
    public void deleteProductionLine(Long id) {
        ProductionLine productionLine = productionLineRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Production line not found!")
        );
        productionLine.setDeletedAt(LocalDateTime.now());
        productionLineRepository.save(productionLine);
    }

    @Override
    public ProductionLineResponse updateProductionLine(Long id, ProductionLineRequest productionLineRequest) {
        ProductionLine productionLine = productionLineRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Production line not found!")
        );
        Department department = departmentRepository.findById(productionLineRequest.deptId()).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found!")
        );
        productionLineMapper.updateFromProductionLineRequest(productionLineRequest, productionLine);
        productionLine.setUpdatedAt(LocalDateTime.now());
        productionLine.setDepartment(department);

        ProductionLine updatedProductionLine = productionLineRepository.save(productionLine);

        return productionLineMapper.toProductionLineResponse(updatedProductionLine);
    }

    @Override
    public ProductionLineResponse createProductionLine(ProductionLineRequest productionLineRequest) {
        Department dept = departmentRepository.findById(productionLineRequest.deptId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found!")
        );

        ProductionLine productionLine = productionLineMapper.fromProductionLineRequest(productionLineRequest);
        productionLine.setDepartment(dept);
        productionLine.setCreatedAt(LocalDateTime.now());
        productionLine.setUpdatedAt(LocalDateTime.now());
        productionLine.setStatus(ProductionLineStatus.inactive);

        ProductionLine savedProductionLine = productionLineRepository.save(productionLine);

        return ProductionLineResponse.builder()
                                    .id(savedProductionLine.getId())
                                    .line(savedProductionLine.getLine())
                                    .image(savedProductionLine.getImage())
                                    .dept(savedProductionLine.getDepartment().getDepartment())
                                    .deptId(savedProductionLine.getDepartment().getId())
                                    .build();
    }

    @Override
    public Page<ProductionLineResponse> getProductionLine(ProductionLineFilterRequest productionLineFilterRequest) {

        if (productionLineFilterRequest.pageNo() <= 0 || productionLineFilterRequest.pageSize() <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no or Page size must be bigger than 0");
        }

        Specification<ProductionLine> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (productionLineFilterRequest.search() != null){
            String searchTerm = "%" + productionLineFilterRequest.search().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("line")), searchTerm)
            );
        }

        if (productionLineFilterRequest.departmentId() != null){
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("department").get("id"), productionLineFilterRequest.departmentId())
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(productionLineFilterRequest.pageNo() - 1, productionLineFilterRequest.pageSize(), sort);
        Page<ProductionLine> productionLines = productionLineRepository.findAll(spec, pageRequest);
        return productionLines.map(productionLine ->
            ProductionLineResponse.builder()
                .id(productionLine.getId())
                .line(productionLine.getLine())
                .image(productionLine.getImage())
                .workers(productionLine.getUsers().size())
                .dept(productionLine.getDepartment().getDepartment())
                .deptId(productionLine.getDepartment().getId())
                .build()
        );
    }
}
