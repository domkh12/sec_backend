package site.secmega.secapi.feature.material.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaterialReportResponse {
    private String code;
    private String name;
    private String description;
    private String styles;
    private String unit;
    private String size;
    private String color;
    private Double balance;
}