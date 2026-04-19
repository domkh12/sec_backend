package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import site.secmega.secapi.base.DepartmentStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class Department extends BaseEntity{
    /**
     * Field
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String department;
    private DepartmentStatus status;

    /**
     * Relationship
     * */
    @OneToMany(mappedBy = "department")
    private List<ProductionLine> productionLines;
    @OneToMany(mappedBy = "department")
    private List<User> users;
}
