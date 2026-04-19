package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "cutting_details")
@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class CuttingDetail extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer cutQty;
    @Column(nullable = false)
    private LocalDateTime cuttingDate;

    /**
     * Relationship
     * */

    @ManyToOne
    private Size size;

    @ManyToOne
    private WorkOrder workOrder;

    @ManyToOne
    private ProductionLine productionLine;
}
