package site.secmega.secapi.feature.materialColor.dto;

import jakarta.validation.constraints.NotBlank;

public record MaterialColorRequest(
        @NotBlank(message = "Name is required!")
        String name
) {
}
