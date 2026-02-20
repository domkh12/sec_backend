package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.*;

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

    // Sewing start time
    private LocalTime sewStart;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "tv")
    private List<TvData> tvDatas;

}
