package site.secmega.secapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table
@Entity(name = "warehouses")
@SQLRestriction("deleted_at IS NULL")
public class Warehouse extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String uuid;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String code;

    @NotBlank
    @Size(max = 155)
    @Column(nullable = false, length = 155, unique = true)
    private String name;

    @Size(max = 255)
    private String address;

    @Size(max = 100)
    private String city;

    @Column(nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "warehouse")
    private List<Rack> racks;
}
