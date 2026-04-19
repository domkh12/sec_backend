package site.secmega.secapi.feature.operation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record OperationResponse(
        Long id,
        String name,
        @JsonFormat(pattern = "dd/MMM/yyyy hh:mma")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd/MMM/yyyy hh:mma")
        LocalDateTime updatedAt
) {
}
