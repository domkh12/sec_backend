package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.secmega.secapi.base.Severity;

import java.time.LocalDateTime;

@Entity
@Table(name = "defect_types")
@Getter
@Setter
@NoArgsConstructor
public class DefectType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private String category;
    private Severity severity;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
