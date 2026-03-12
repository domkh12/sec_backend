package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import site.secmega.secapi.base.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class User extends BaseEntity{

    /**
     * Core Identity Information
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String employeeId;
    private String email;
    private String firstName;
    private String lastName;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String phoneNumber;
    private LocalDateTime hireDate;
    private String avatar;
    private LocalDateTime lastLogin;
    private LocalDate dateOfBirth;
    private UserStatus status;

    /**
     * Security
     * These align with Spring Security's UserDetails interface requirements.
     * */
    private Boolean isAccountNonExpired = true;
    private Boolean isAccountNonLocked = true;
    private Boolean isCredentialsNonExpired = true;
    private Boolean isEnabled = true;

    /*
     * Authorization & Relationships
     * Many-to-Many mapping for Role-Based Access Control (RBAC).
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<Role> roles;

    @ManyToOne
    private ProductionLine productionLine;
    @ManyToOne
    private Shift shift;
    @OneToMany(mappedBy = "user")
    private List<HourlyProduction> hourlyProductions;
    @OneToMany(mappedBy = "user")
    private List<DowntimeRecord> downtimeRecords;

}
