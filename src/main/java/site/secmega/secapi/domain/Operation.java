package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Table(name = "operations")
@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class Operation extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    /**
     * Relationship
     * */
//    @OneToMany(mappedBy = "operation")
//    private List<ProcessingDetail> processingDetails;
//    @ManyToOne
//    private ProductionLine productionLine;
}
