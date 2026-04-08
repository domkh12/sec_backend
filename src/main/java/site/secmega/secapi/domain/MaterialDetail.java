package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import site.secmega.secapi.base.TransactionType;

import java.time.LocalDateTime;

@Entity
@Table(name = "material_details")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class MaterialDetail extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double quantity;
    private LocalDateTime transactionDate;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private Double qtyBalance;

    /**
     * Relationship
     * */
    @ManyToOne
    private Material material;
    @ManyToOne
    private User user;
}
