package site.secmega.secapi.feature.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Query("select count(u) from User u inner join u.roles roles where roles.id = ?1")
    Integer countByRoles_Id(Integer id);

    @Query("select count(u) from User u where u.productionLine is not null")
    Integer countByProductionLineNotNull();

    // Create checks
    boolean existsByUsernameIgnoreCaseAndDeletedAtIsNull(String username);
    boolean existsByPhoneNumberAndDeletedAtIsNull(String phoneNumber);
    boolean existsByEmployeeIdAndDeletedAtIsNull(String employeeId);

    // Update checks (exclude self)
    boolean existsByUsernameIgnoreCaseAndIdNotAndDeletedAtIsNull(String username, Long id);
    boolean existsByPhoneNumberAndIdNotAndDeletedAtIsNull(String phoneNumber, Long id);
    boolean existsByEmployeeIdAndIdNotAndDeletedAtIsNull(String employeeId, Long id);


    Optional<User> findByUsername(String username);

}
