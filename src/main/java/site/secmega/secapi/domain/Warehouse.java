package site.secmega.secapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table
@Entity(name = "warehouses")
@SQLRestriction("deleted_at IS NULL")
@EntityListeners(AuditingEntityListener.class)
public class Warehouse extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 155, unique = true)
    private String name;

    private String address;

    private String city;

    @Column(nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "warehouse")
    private List<Rack> racks;
}
