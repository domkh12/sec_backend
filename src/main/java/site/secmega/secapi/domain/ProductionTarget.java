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
    /**
     * Field
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private TargetType targetType;
    @Column(nullable = false)
    private LocalDateTime targetDate;
    @Column(nullable = false)
    private Integer targetQuantity;
    private Double maxDefectRate;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    private ProductionLine productionLine;
    @ManyToOne
    private Product product;
}
