package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "display_configs")
@Getter
@Setter
@NoArgsConstructor
public class DisplayConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String displayName;
    private String lineIds;
    private Integer refreshIntervalSeconds;
    private Boolean showHourly;
    private Boolean showDaily;
    private Boolean showDefectDetails;
    private Boolean showEfficiency;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
