package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @ManyToMany(mappedBy = "roles")
    private List<User> users;

//    @Override
//    public String getAuthority() {
//        return "ROLE_" + name;
//    }


}
