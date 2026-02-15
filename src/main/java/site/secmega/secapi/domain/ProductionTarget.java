package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.secmega.secapi.base.TargetType;

import java.time.LocalDateTime;

@Entity
@Table(name = "production_targets")
@Getter
@Setter
@NoArgsConstructor
public class ProductionTarget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer lineId;
    private Integer productId;
    private TargetType targetType;
    private LocalDateTime targetDate;
    private Integer targetQuantity;
    private Double maxDefectRate;
    private LocalDateTime createdAt;
}
