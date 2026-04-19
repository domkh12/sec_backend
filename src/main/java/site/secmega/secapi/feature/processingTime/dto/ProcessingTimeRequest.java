package site.secmega.secapi.feature.processingTime.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public record ProcessingTimeRequest(
        @NotBlank(message = "Style is required")
        String style,
        @NotNull(message = "Operation is required")
        List<ProcessOperationRequest> operation
) {
}
