package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "shifts")
@Getter
@Setter
@NoArgsConstructor
public class Shift {

    /**
     * Field
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private LocalTime startTime;
    @Column(nullable = false)
    private LocalTime endTime;
    private Integer breakDurationMinutes;
    @Column(nullable = false)
    private Boolean isActive;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * Relationship
     * */
//    @OneToMany(mappedBy = "shift")
//    private List<User> users;
//    @OneToMany(mappedBy = "shift")
//    private List<HourlyProduction> hourlyProductions;
}
