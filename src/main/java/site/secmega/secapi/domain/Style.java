package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import site.secmega.secapi.base.StyleStatus;

import java.util.List;

@Entity
@Table(name = "styles")
@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class Style extends BaseEntity{
    /**
     * Field
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String styleNo;
    private String description;
    private StyleStatus status;

    /**
     * Relationship
     * */
    @OneToMany(mappedBy = "style")
    private List<PurchaseOrder> purchaseOrders;
    @OneToMany(mappedBy = "style")
    private List<WorkOrder> workOrders;
}
