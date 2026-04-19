package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Table(name = "processing_details")
@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class ProcessingDetail extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderingNumber;
    /**
     * Relationship
     * */
//    @ManyToOne
//    private ProcessingTime processingTime;
//    @ManyToOne
//    private Operation operation;
//    @OneToMany(mappedBy = "processingDetail")
//    private List<WorkOrderDetail> workOrderDetails;
}
