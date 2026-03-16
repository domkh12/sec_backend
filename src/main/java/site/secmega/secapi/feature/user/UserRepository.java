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

    @Query("select u from User u where u.id <> ?1")
    Page<User> findByIdNot(Long id, Pageable pageable);

    @Query("""
            select u from User u
            where u.id <> ?1 or upper(u.employeeId) like upper(concat('%', ?2, '%')) or upper(u.firstName) like upper(concat('%', ?2, '%')) or upper(u.lastName) like upper(concat('%', ?2, '%')) or upper(u.phoneNumber) like upper(concat('%', ?2, '%'))""")
    Page<User> findAllAndFilterUser(Long id, String search, Pageable pageable);


    Optional<User> findByUsername(String username);

}
