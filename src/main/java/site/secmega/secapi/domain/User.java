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
    @Column(nullable = false, unique = true)
    private String uuid;
    private String employeeId;
    @Column(nullable = false)
    private String nameEn;
    @Column(nullable = false)
    private String nameKh;
    @Column(nullable = false)
    private String gender;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private String phoneNumber;
    private String position;
    private LocalDate hireDate;
    private String avatar;
    private LocalDateTime lastLogin;
    private LocalDate dateOfBirth;
    private UserStatus status;
    private Boolean isDepartmentHead = false;

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
    private Department department;
    @OneToMany(mappedBy = "user")
    private List<MaterialDetail> materialDetails;

}
