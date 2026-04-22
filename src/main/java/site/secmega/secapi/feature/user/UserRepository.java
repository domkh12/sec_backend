package site.secmega.secapi.feature.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {



    @Query("select count(u) from User u where u.productionLine is not null")
    Integer countByProductionLineNotNull();

    // Create checks
    boolean existsByUsernameIgnoreCaseAndDeletedAtIsNull(String username);
    boolean existsByPhoneNumberAndDeletedAtIsNull(String phoneNumber);
    boolean existsByEmployeeIdAndDeletedAtIsNull(String employeeId);
    boolean existsByEmailIgnoreCaseAndDeletedAtIsNull(String email);

    // Update checks (exclude self)
    boolean existsByUsernameIgnoreCaseAndIdNotAndDeletedAtIsNull(String username, Long id);
    boolean existsByPhoneNumberAndIdNotAndDeletedAtIsNull(String phoneNumber, Long id);
    boolean existsByEmployeeIdAndIdNotAndDeletedAtIsNull(String employeeId, Long id);
    boolean existsByEmailIgnoreCaseAndIdNotAndDeletedAtIsNull(String email, Long id);


    Optional<User> findByUsername(String username);

}
