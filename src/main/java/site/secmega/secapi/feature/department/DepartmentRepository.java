package site.secmega.secapi.feature.department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.base.DepartmentStatus;
import site.secmega.secapi.domain.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {
    @Query("select count(d) from Department d where d.status = ?1")
    Integer countByStatus(DepartmentStatus status);

    @Query("select count(d) from Department d")
    Integer countDepartment();

    @Query("select (count(d) > 0) from Department d where upper(d.department) = upper(?1) and d.deletedAt is null")
    boolean existsByDepartmentIgnoreCaseAndDeletedAtNull(String department);

    @Query("""
            select (count(d) > 0) from Department d
            where upper(d.department) = upper(?1) and d.id <> ?2 and d.deletedAt is null""")
    boolean existsByDepartmentIgnoreCaseAndIdNotAndDeletedAtNull(String department, Long id);

}
