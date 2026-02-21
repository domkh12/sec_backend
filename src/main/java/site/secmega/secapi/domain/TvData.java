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

    // Actual output (ACT)
    private Integer actualOutput;

    // Defects

    // Performance
    private Double finishPercentage;
    private Integer finish;
    private Integer yesterdayFinish;
    private Double defectPercentage;

    private String date;
    private Integer dTarget;
    private Integer nowTarget;
    private Integer defectQty;
    private Boolean isToday;

    // Hourly outputs (8:00 — 18:00)

    private Integer h8;
    private Integer h9;
    private Integer h10;
    private Integer h11;
    private Integer h13;
    private Integer h14;
    private Integer h15;
    private Integer h16;
    private Integer h17;
    private Integer h18;

    @ManyToOne
    private Tv tv;
}
