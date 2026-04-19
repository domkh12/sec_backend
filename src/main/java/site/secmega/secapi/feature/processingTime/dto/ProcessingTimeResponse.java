package site.secmega.secapi.feature.processingTime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import site.secmega.secapi.feature.operation.dto.OperationResponse;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ProcessingTimeResponse(
        Long id,
        String style,
        @JsonFormat(pattern = "dd/MMM/yyy hh:mma")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd/MMM/yyy hh:mma")
        LocalDateTime updatedAt,
        List<ProcessOperationResponse> operation
) {
}
