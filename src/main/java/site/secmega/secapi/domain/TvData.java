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

    // Production date
    @Column(nullable = false)
    private LocalDate day;

    // Actual output (ACT)
    private Integer actualOutput;

    // Defects


    // Performance
    private Double finishPercentage;
    private Integer finish;
    private Integer yesterdayFinish;
    private Double defectPercentage;

    private String recordDate;
    private Integer workHour;
    private Integer dayTarget;
    private Integer hourTarget;
    private Integer input;
    private Integer nowTarget;
    private Integer defectQty;

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
