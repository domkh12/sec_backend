package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import site.secmega.secapi.base.ProductStatus;

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
    private String styleNo;
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
    private List<Bundle> bundles;
    @ManyToOne
    private SubCategory subCategory;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "size_id", referencedColumnName = "id")
    )
    private List<Size> sizes;
    @OneToMany(mappedBy = "product")
    private List<ProductSku> productSkus;
}
