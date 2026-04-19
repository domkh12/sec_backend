package site.secmega.secapi.mapper;

import org.mapstruct.Mapper;
import site.secmega.secapi.domain.Operation;
import site.secmega.secapi.feature.operation.dto.OperationRequest;
import site.secmega.secapi.feature.operation.dto.OperationResponse;

@Mapper(componentModel = "spring")
public interface OperationMapper {
    OperationResponse toOperationResponse(Operation operation);
    Operation fromOperationRequest(OperationRequest operationRequest);
}
