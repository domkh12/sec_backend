package site.secmega.secapi.feature.department.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record DepartmentResponse(
        Long id,
        String name,
        @JsonFormat(pattern = "dd/MMM/yyyy hh:mma")
        LocalDateTime createdAt
) {
}
