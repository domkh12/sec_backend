package site.secmega.secapi.feature.material.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaterialReportStockInResponse {
    private String date;
    private Double quantity;
    private Double qtyBalance;
    private String color;
    private String size;
    private String material;
    private String code;
    private String unit;
    private String nameReceiver;
    private String style;
}
