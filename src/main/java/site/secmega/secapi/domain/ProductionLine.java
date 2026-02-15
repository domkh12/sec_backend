package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import site.secmega.secapi.base.ProductionLineStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "production_lines")
@Getter
@Setter
public class ProductionLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String code;
    private String department;
    private Integer capacityPerHour;
    private ProductionLineStatus status;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "productionLine")
    private List<User> users;

}
