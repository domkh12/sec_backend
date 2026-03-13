package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {
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
    private String category;
    private String size;
    private String color;
    private Integer targetProductionPerHour;
    private Double standardMinuteValue;
    private String description;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Relationship
     * */
    @OneToMany(mappedBy = "product")
    private List<HourlyProduction> hourlyProductions;
    @OneToMany(mappedBy = "product")
    private List<ProductionTarget> productionTargets;
}
