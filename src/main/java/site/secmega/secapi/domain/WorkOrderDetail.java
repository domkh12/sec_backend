package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "work_order_details")
@Getter
@Setter
@NoArgsConstructor
public class WorkOrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;


    @ManyToOne
    private ProductSku productSku;
    @ManyToOne
    private WorkOrder workOrder;
}
