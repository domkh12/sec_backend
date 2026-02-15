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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer hourProductionId;
    private Integer defectTypeId;
    private Integer qtyDefect;
    private String notes;
    private LocalDateTime createdAt;
}
