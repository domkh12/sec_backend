package site.secmega.secapi.feature.department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
