package site.secmega.secapi.feature.outputDetail.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutputReportResponse{
    private String reportDate;
    private String mo;
    private String po;
    private String style;
    private String buyer;
    private String size;
    private String goodQty;
    private String outputDate;
    private String image;
    private String fromDate;
    private String toDate;
}
