package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "hourly_productions")
@Getter
@Setter
@NoArgsConstructor
public class HourlyProduction {
    /**
     * Field
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime productionDate;
    @Column(nullable = false)
    private Integer hourNumber;
    private Integer qtyGood;
    private Integer qtyDefect;
    private Integer qtyRework;
    private Integer qtyTotal;
    private Double efficiencyRate;
    private Integer operatorCount;
    @Column(nullable = false)
    private LocalDateTime entryTime;
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Relationship
     * */
//    @ManyToOne
//    private ProductionLine productionLines;
//    @ManyToOne
//    private Shift shift;
//    @ManyToOne
//    private Product product;
//    @ManyToOne
//    private User user;
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(joinColumns = @JoinColumn(name = "hourly_production_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "defect_type_id", referencedColumnName = "id")
//    )
//    private List<DefectType> defectTypes;
}
