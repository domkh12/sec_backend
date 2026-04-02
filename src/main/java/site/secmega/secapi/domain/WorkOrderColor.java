package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Table(name = "work_order_colors")
@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class WorkOrderColor extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relationship
     * */
    @ManyToOne
    private WorkOrder workOrder;

    @ManyToOne
    private Color color;
    @OneToMany(mappedBy = "workOrderColor")
    private List<WorkOrderColorSize> workOrderColorSizes;
}
