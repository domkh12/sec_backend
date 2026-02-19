package site.secmega.secapi.feature.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
