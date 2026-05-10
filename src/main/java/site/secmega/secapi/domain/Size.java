package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Table(name = "sizes")
@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class Size extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String size;

    /**
     * Relationship
     * */
    @ManyToMany(mappedBy = "sizes")
    private List<WorkOrder> workOrders;

    @OneToMany(mappedBy = "size")
    private List<OutputDetail> outputDetails;

    @OneToMany(mappedBy = "size")
    private List<Bundle> bundles;

    @OneToMany(mappedBy = "size")
    private List<Material> materials;
}
