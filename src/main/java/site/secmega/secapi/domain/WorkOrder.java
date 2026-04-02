package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

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
    @Column(nullable = false)
    private Integer qty;
    private Integer totalInput;
    private Integer totalOutput;
    private Integer totalQty;

    @ManyToOne
    private Buyer buyer;

    @OneToMany(mappedBy = "workOrder")
    private List<WorkOrderColor> workOrderColors;

}
