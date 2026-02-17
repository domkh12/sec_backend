package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "defect_details")
@Getter
@Setter
@NoArgsConstructor
public class DefectDetail {
    /**
     * Field
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer qtyDefect;
    private String notes;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * Relationship
     * */
    @ManyToOne
    private HourlyProduction hourlyProduction;
    @ManyToOne
    private DefectType defectType;

}
