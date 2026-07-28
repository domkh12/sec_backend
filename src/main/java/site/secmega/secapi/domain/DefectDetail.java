package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Table(name = "defect_details")
@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class DefectDetail extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer defectQty;
    private LocalDate defectDate;
    /**
     * Relationship
     * */
    @ManyToOne
    private Time time;

    @ManyToOne
    private DefectType defectType;

    @ManyToOne
    private WorkOrder workOrder;

    @ManyToOne
    private ProductionLine productionLine;
}
