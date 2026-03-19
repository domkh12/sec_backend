package site.secmega.secapi.feature.department.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import site.secmega.secapi.base.DepartmentStatus;

import java.time.LocalDateTime;

public record DepartmentResponse(
        Long id,
        String department,
        String head,
        Integer lines,
        Integer workers,
        DepartmentStatus status,
        @JsonFormat(pattern = "dd/MMM/yyyy hh:mma")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd/MMM/yyyy hh:mma")
        LocalDateTime updatedAt
) {
}
