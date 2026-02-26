package site.secmega.secapi.feature.department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query("select (count(d) > 0) from Department d where d.name = ?1")
    boolean existsByName(String name);

}
