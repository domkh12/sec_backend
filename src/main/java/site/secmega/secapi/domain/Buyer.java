package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Table(name = "buyers")
@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class Buyer extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private List<String> files;

    @OneToMany(mappedBy = "buyer")
    private List<WorkOrder> workOrders;
}
