package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "hourly_productions")
@Getter
@Setter
@NoArgsConstructor
public class HourlyProduction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime productionDate;
    private Integer hourNumber;
    private Integer qtyGood;
    private Integer qtyDefect;
    private Integer qtyRework;
    private Integer qtyTotal;
    private Double efficiencyRate;
    private Integer operatorCount;

    private Integer line_id;
    private Integer shift_id;
    private Integer product_id;

}
