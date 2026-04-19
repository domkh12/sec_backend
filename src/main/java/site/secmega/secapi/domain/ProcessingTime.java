package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "processing_times")
@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class ProcessingTime extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String style;

    /**
     * Relationship
     * */
//    @OneToMany(mappedBy = "processingTime")
//    private List<ProcessingDetail> processingDetails = new ArrayList<>();
}
