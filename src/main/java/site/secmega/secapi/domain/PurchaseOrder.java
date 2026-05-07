package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import site.secmega.secapi.base.POStatus;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "purchase_orders")
@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class PurchaseOrder extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String po;
    @Column(nullable = false)
    private Integer qty;
    @Column(nullable = false)
    private LocalDate shipmentDate;
    @Column(nullable = false)
    private POStatus status;

    /**
     * Relationship
     * */
    @ManyToOne
    private Style style;

    @OneToMany(mappedBy = "purchaseOrder")
    private List<WorkOrder> workOrders;

    @ManyToOne
    private Buyer buyer;

    public POStatus getStatus() {
        if (this.shipmentDate != null && this.shipmentDate.isBefore(LocalDate.now())) {
            return POStatus.DELAYED; // Assuming DELAYED exists in your POStatus enum
        }
        return this.status;
    }
}
