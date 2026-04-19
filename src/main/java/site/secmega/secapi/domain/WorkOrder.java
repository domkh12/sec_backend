package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import site.secmega.secapi.base.WorkOrderStatus;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "work_orders")
@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class WorkOrder extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String mo;
    private String style;
    @Column(nullable = false)
    private Integer qty;
    @Column(nullable = false)
    private WorkOrderStatus status;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    private String image;
    private String orderFollower;

    @ManyToOne
    private Buyer buyer;

    @ManyToOne
    private Color color;

    @ManyToOne
    private Product product;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "wo_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "size_id", referencedColumnName = "id")
    )
    private List<Size> sizes;

    @OneToMany(mappedBy = "workOrder")
    private List<OutputDetail> outputDetails;

    @OneToMany(mappedBy = "workOrder")
    private List<CuttingDetail> cuttingDetails;

    @OneToMany(mappedBy = "workOrder")
    private List<Bundle> bundles;

}
