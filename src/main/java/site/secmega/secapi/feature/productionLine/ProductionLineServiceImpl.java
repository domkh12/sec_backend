package site.secmega.secapi.feature.productionLine;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.base.ProductionLineStatus;
import site.secmega.secapi.domain.Department;
import site.secmega.secapi.domain.ProductionLine;
import site.secmega.secapi.feature.department.DepartmentRepository;
import site.secmega.secapi.feature.productionLine.dto.ProductionLineRequest;
import site.secmega.secapi.feature.productionLine.dto.ProductionLineResponse;
import site.secmega.secapi.mapper.ProductionLineMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductionLineServiceImpl implements ProductionLineService{

    private final ProductionLineRepository productionLineRepository;
    private final ProductionLineMapper productionLineMapper;
    private final DepartmentRepository departmentRepository;

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

        return productionLineMapper.toProductionLineResponse(savedProductionLine);
    }

    @Override
    public Page<ProductionLineResponse> getProductionLine(Integer pageNo, Integer pageSize) {

        if (pageNo <= 0 || pageSize <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no or Page size must be bigger than 0");
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<ProductionLine> productionLines = productionLineRepository.findAll(pageRequest);

        return productionLines.map(productionLineMapper::toProductionLineResponse);
    }
}
