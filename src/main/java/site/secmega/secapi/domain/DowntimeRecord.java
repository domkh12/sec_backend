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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer line_id;
    private LocalDateTime downTimeDate;
    private Integer startHour;
    private Integer endHour;
    private String reason;
    private DowntimeType downtimeType;
    private Integer minuteLost;
    private Integer resolvedBy;
    private LocalDateTime createdAt;
}
