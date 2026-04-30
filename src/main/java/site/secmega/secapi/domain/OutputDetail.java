package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "output_details")
@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class OutputDetail extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer goodQty;
    @Column(nullable = false)
    private Integer defectQty;
    @Column(nullable = false)
    private LocalDateTime outputDate;

    /**
     * Relationship
     * */
    @ManyToOne
    private Bundle bundle;

    @ManyToOne
    private Size size;

    @ManyToOne
    private WorkOrder workOrder;

    @ManyToOne
    private ProductionLine productionLine;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "ouput_detail_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "defect_type_id", referencedColumnName = "id")
    )
    private List<DefectType> defectTypes;

    @ManyToOne
    private Time time;
}
