package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import site.secmega.secapi.base.MaterialStatus;

import java.util.List;

@Entity
@Table(name = "materials")
@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class Material extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private String unit;
    private String image;
    private Double balance;
    private MaterialStatus status;

    /**
     * Relationship
     * */
    @OneToMany(mappedBy = "material")
    private List<MaterialDetail> materialDetails;

    public void setBalance(Double balance) {
        this.balance = balance;
        syncStatus();
    }

    private void syncStatus() {
        if (balance == null || balance <= 0) {
            this.status = MaterialStatus.OUT_OF_STOCK;
        } else if (balance <= 10) {
            this.status = MaterialStatus.LOW_STOCK;
        } else {
            this.status = MaterialStatus.AVAILABLE;
        }
    }
}
