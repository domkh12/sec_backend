package site.secmega.secapi.feature.department.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record DepartmentResponse(
        Long id,
        String department,
        @JsonFormat(pattern = "dd/MMM/yyyy hh:mma")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd/MMM/yyyy hh:mma")
        LocalDateTime updatedAt
) {
}
