package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "tvs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;

    private String line;
    // Number of workers
    private String orderNo;
    private Integer worker;
    private Integer orderQty;
    private Integer totalInLine;
    private Integer totalOutput;
    private Integer orderInline;
    private Integer balanceInLine;
    private Integer qcRepairBack;
    private Integer balanceDay;
    private String sewD;
    private String styleNo;
    private LocalDate startDate;
    private LocalDate finishDate;

    // Sewing start time
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private Integer input;
    private Integer wHour;
    private Integer hTarg;

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

    @OneToMany(mappedBy = "tv")
    private List<TvData> tvDatas;

}
