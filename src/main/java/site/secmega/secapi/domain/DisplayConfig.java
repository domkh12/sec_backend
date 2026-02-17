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

    /**
     * Field
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String displayName;
    private String lineIds;  //Comma-separated line IDs to display
    private Integer refreshIntervalSeconds;
    @Column(nullable = false)
    private Boolean showHourly;
    @Column(nullable = false)
    private Boolean showDaily;
    @Column(nullable = false)
    private Boolean showDefectDetails;
    @Column(nullable = false)
    private Boolean showEfficiency;
    @Column(nullable = false)
    private Boolean isActive;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
