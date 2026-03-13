package site.secmega.secapi.feature.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Role;
import site.secmega.secapi.feature.role.dto.RoleResponse;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

}
