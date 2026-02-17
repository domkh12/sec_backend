package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.secmega.secapi.base.DowntimeType;

import java.time.LocalDateTime;

@Entity
@Table(name = "downtime_records")
@Getter
@Setter
@NoArgsConstructor
public class DowntimeRecord {
    /**
     * Field
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime downTimeDate;
    @Column(nullable = false)
    private Integer startHour;
    private Integer endHour;
    private String reason;
    @Column(nullable = false)
    private DowntimeType downtimeType;
    private Integer minuteLost;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * Relationship
     * */
    @ManyToOne
    private ProductionLine productionLine;
    @ManyToOne
    private User user;
}
