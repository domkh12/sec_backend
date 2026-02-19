package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "tv_datas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TvData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Line name (ex: F1, F2, Line A)
    @Column(nullable = false, length = 50)
    private String line;

    // Style number
    @Column(nullable = false, length = 100)
    private String styleNo;

    // Sewing start time
    private LocalTime sewStart;

    // Production date
    @Column(nullable = false)
    private LocalDate day;

    // Number of workers
    private Integer worker;

    // Actual output (ACT)
    private Integer actualOutput;

    // Targets
    private Integer targetHour;
    private Integer targetDay;
    private Integer targetNow;

    // Defects
    private Integer defect;

    // Performance
    private Double finishPercentage;
    private Integer finish;
    private Integer yesterdayFinish;
    private Double defectPercentage;

    // Hourly outputs (8:00 — 18:00)

    private Integer output08;
    private Integer output09;
    private Integer output10;
    private Integer output11;
    private Integer output13;
    private Integer output14;
    private Integer output15;
    private Integer output16;
    private Integer output17;
    private Integer output18;

    @ManyToOne
    private Tv tv;
}
