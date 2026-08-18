package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Table
@Entity(name = "receipts")
@SQLRestriction("deleted_at IS NULL")
@EntityListeners(AuditingEntityListener.class)
public class Receipt extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String uuid;
    private String receiptNo;
    private LocalDateTime receiptDate;
    private String status;
    private Double totalQty;
    private String approvedBy;
    private LocalDateTime approvedDate;
    private String remark;

    @ManyToOne
    private PurchaseOrder purchaseOrder;

    @ManyToOne
    private Supplier supplier;
}
