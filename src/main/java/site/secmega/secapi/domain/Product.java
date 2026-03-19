package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import site.secmega.secapi.base.ProductStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class Product extends BaseEntity{
    /**
     * Field
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private String styleName;
    private String size;
    private String color;
    private Integer targetProductionPerHour;
    private Double standardMinuteValue;
    private String description;
    private ProductStatus status;

    /**
     * Relationship
     * */
    @OneToMany(mappedBy = "product")
    private List<HourlyProduction> hourlyProductions;
    @OneToMany(mappedBy = "product")
    private List<ProductionTarget> productionTargets;
    @OneToMany(mappedBy = "product")
    private List<Bundle> bundles;
    @ManyToOne
    private SubCategory subCategory;
}
