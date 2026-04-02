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
    @ManyToMany(mappedBy = "sizes")
    private List<Product> products;
    @ManyToMany
    @JoinTable(name = "color_size", joinColumns = @JoinColumn(name = "size_id"), inverseJoinColumns = @JoinColumn(name = "color_id"))
    private List<Color> colors;
    @OneToMany(mappedBy = "size")
    private List<WorkOrderColorSize> workOrderColorSizes;
}
