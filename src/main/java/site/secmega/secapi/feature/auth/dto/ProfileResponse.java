package site.secmega.secapi.feature.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ProfileResponse(
        Long id,
        String username,
        String firstName,
        String lastName,
        String phoneNumber,
        @JsonFormat(pattern = "dd/MMM/yyyy")
        LocalDate dateOfBirth,
        String role,
        String avatar,
        String nameEn,
        String nameKh
) {
}
