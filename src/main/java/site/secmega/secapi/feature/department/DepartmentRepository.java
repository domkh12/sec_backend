package site.secmega.secapi.feature.department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query("select (count(d) > 0) from Department d where upper(d.department) = upper(?1) and d.id <> ?2")
    boolean existsByDepartmentIgnoreCaseAndIdNot(String department, Long id);


    @Query("select (count(d) > 0) from Department d where upper(d.department) = upper(?1)")
    boolean existsByDepartmentIgnoreCase(String department);


}
