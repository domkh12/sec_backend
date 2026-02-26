package site.secmega.secapi.feature.department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query("select (count(d) > 0) from Department d where upper(d.name) = upper(?1) and d.id <> ?2")
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);


    @Query("select (count(d) > 0) from Department d where upper(d.name) = upper(?1)")
    boolean existsByNameIgnoreCase(String name);


}
