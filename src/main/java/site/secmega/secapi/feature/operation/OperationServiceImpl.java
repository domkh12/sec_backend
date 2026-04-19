package site.secmega.secapi.feature.operation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.Operation;
import site.secmega.secapi.feature.operation.dto.OperationFilterRequest;
import site.secmega.secapi.feature.operation.dto.OperationResponse;
import site.secmega.secapi.mapper.OperationMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService{

    private final OperationRepository operationRepository;
    private final OperationMapper operationMapper;

    @Override
    public Page<OperationResponse> findAll(OperationFilterRequest operationFilterRequest) {
        if (operationFilterRequest.pageNo() <= 0 || operationFilterRequest.pageSize() <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no and Page size must be greater than 0");
        }

        Specification<Operation> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (operationFilterRequest.search() != null){
            String searchTerm = "%" + operationFilterRequest.search().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), searchTerm)
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(operationFilterRequest.pageNo() - 1, operationFilterRequest.pageSize(), sort);
        Page<Operation> operations = operationRepository.findAll(spec, pageRequest);

        return operations.map(operationMapper::toOperationResponse);
    }

    @Override
    public List<OperationResponse> findLookup() {

        List<Operation> operations = operationRepository.findAll();

        return operations.stream().map(operationMapper::toOperationResponse).toList();
    }
}
