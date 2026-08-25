package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.secmega.secapi.base.MaterialStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "materials")
@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
@EntityListeners(AuditingEntityListener.class)
public class Material extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false, unique = true)
    private String barcode;
    @Column(nullable = false)
    private String name;
    private String dyeLot;
    private String rollNo;
    private Double receivedQty;
    private Double balanceQty;
    private LocalDateTime receivedDate;
    private String receivedBy;
    private String image;
    private MaterialStatus status;

    /**
     * Relationship
     * */
    @ManyToOne
    private Color color;

    @ManyToOne
    private Size size;

    @OneToMany(mappedBy = "material")
    private List<MaterialDetail> materialDetails;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "material_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "style_id", referencedColumnName = "id")
    )
    private List<Style> styles;

    @ManyToOne
    private Unit unit;

    @OneToMany(mappedBy = "material")
    private List<ReceiptDetail> receiptDetails;
}
