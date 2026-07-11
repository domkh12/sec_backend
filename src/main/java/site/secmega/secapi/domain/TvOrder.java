package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tv_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TvOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNo;
    private String orderQty;
    private String status;
    private Integer totalInLine;
    private Integer totalOutput;
    private Integer orderInline;
    private Integer balanceInLine;
    private Integer qcRepairBack;
    private Integer balanceDay;
    private Integer input;
    private Integer wHour;
    private Integer hTarg;
    private LocalDate startDate;
    private LocalDate finishDate;

    // -- Relationship
    @ManyToOne
    private Tv tv;

    @OneToMany(mappedBy = "tvOrder")
    private List<TvData> tvDatas;

    @ManyToOne
    private Style style;
}
