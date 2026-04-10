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
    @Column(nullable = false)
    private Integer qty;
    @Column(nullable = false)
    private WorkOrderStatus status;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    private String image;

    @ManyToOne
    private Buyer buyer;

    @ManyToOne
    private Color color;

    @ManyToOne
    private Product product;

}
