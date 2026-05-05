package site.secmega.secapi.feature.style.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record StyleRequest(
        @NotBlank(message = "Style cannot blank")
        String styleNo,
        String description
) {
    
}
