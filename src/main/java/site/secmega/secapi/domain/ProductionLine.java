package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import site.secmega.secapi.base.ProductionLineStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "production_lines")
@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class ProductionLine extends BaseEntity{
    /**
     * Information
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String line;
    @Column(nullable = false)
    private Boolean isInput;
    @Column(nullable = false)
    private ProductionLineStatus status;
    private String image;

    /**
     * Relationship
     * */
    @ManyToOne
    private Department department;

    @OneToMany(mappedBy = "productionLine")
    private List<User> users;

    @OneToMany(mappedBy = "productionLine")
    private List<CuttingDetail> cuttingDetails;

    @OneToMany(mappedBy = "productionLine")
    private List<OutputDetail> outputDetails;

    @OneToMany(mappedBy = "productionLine")
    private List<Bundle> bundles;
}
