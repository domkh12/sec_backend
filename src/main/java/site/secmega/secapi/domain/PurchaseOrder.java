package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import site.secmega.secapi.base.PORemark;
import site.secmega.secapi.base.POStatus;

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
    private String poNumber;
    private Integer orderQty;
    private String shipmentDate;
    private POStatus status;
    private PORemark remark;

    @ManyToOne
    private Buyer buyer;

    @OneToMany(mappedBy = "purchaseOrder")
    private List<OrderDetail> orderDetail;

}
