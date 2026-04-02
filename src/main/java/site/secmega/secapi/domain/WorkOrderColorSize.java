package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "work_order_color_sizes")
@Getter
@Setter
@NoArgsConstructor
public class WorkOrderColorSize extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer qty;

    @ManyToOne
    private WorkOrderColor workOrderColor;
    @ManyToOne
    private Size size;
}
