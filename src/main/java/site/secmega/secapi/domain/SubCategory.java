package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Table(name = "sub_categories")
@Getter
@Setter
@SQLRestriction("deleted_at IS NULL")
public class SubCategory extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    /**
     * Relationship
     * */
    @ManyToOne
    private Category category;
    @OneToMany(mappedBy = "subCategory")
    private List<Product> products;
}
