package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.secmega.secapi.base.Severity;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "defect_types")
@Getter
@Setter
@NoArgsConstructor
public class DefectType {
    /**
     * Field
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false)
    private String name;
    private String category;
    @Column(nullable = false)
    private Severity severity;
    private String description;
    @Column(nullable = false)
    private Boolean isActive;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * Relationship
     * */
    @OneToMany(mappedBy = "defectType")
    private List <DefectDetail> defectDetails;

}
