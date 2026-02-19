package site.secmega.secapi.feature.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select (count(u) > 0) from User u where u.username = ?1")
    boolean existsByUsername(String username);

    @Query("select (count(u) > 0) from User u where u.phoneNumber = ?1")
    boolean existsByPhoneNumber(Integer phoneNumber);

    @Query("select (count(u) > 0) from User u where u.employee_id = ?1")
    boolean existsByEmployee_id(String employee_id);


    Optional<User> findByUsername(String username);

}
