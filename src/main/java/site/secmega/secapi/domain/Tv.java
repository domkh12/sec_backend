package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tvs")
@Getter
@Setter
public class Tv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "tv")
    private List<TvData> tvDatas;

}
