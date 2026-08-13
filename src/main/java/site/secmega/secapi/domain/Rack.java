package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table
@Entity(name = "racks")
public class Rack extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String qrCode;
    private String code;
    private Boolean isActive;

    @OneToMany(mappedBy = "rack")
    private List<Carton> cartons;

    @ManyToOne
    private Warehouse warehouse;

}
