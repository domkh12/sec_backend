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

    private String date;
    private Integer dTarget;
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

    // Defect Hour
    private Integer dh8;
    private Integer dh9;
    private Integer dh10;
    private Integer dh11;
    private Integer dh13;
    private Integer dh14;
    private Integer dh15;
    private Integer dh16;
    private Integer dh17;
    private Integer dh18;

    @ManyToOne
    private Tv tv;
}
