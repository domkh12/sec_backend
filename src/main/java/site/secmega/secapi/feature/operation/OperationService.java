package site.secmega.secapi.feature.operation;

import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.operation.dto.OperationFilterRequest;
import site.secmega.secapi.feature.operation.dto.OperationResponse;

import java.util.List;

public interface OperationService {
    Page<OperationResponse> findAll(OperationFilterRequest operationFilterRequest);

    List<OperationResponse> findLookup();
}
