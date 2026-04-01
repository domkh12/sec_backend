package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.secmega.secapi.base.ProductionLineStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "production_lines")
@Getter
@Setter
@NoArgsConstructor
public class ProductionLine {
    /**
     * Information
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String line;
    @Column(nullable = false)
    private ProductionLineStatus status;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Relationship
     * */
    @ManyToOne
    private Department department;
    @OneToMany(mappedBy = "productionLine")
    private List<User> users;
    @OneToMany(mappedBy = "productionLines")
    private List<HourlyProduction> hourlyProduction;
}
