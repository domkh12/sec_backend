package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import site.secmega.secapi.base.BundleStatus;

@Entity
@Table(name = "bundles")
@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class Bundle extends BaseEntity{
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true)
    private String bundleCode;

    private Integer bundleQty;

    @Enumerated(EnumType.STRING)
    private BundleStatus status;

    /**
     * Relationship
     * */
    @ManyToOne
    private WorkOrder workOrder;

    @ManyToOne
    private Size size;

    @ManyToOne
    private ProductionLine productionLine;
}
