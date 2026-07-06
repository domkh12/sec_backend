package site.secmega.secapi.feature.defectType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.DefectType;

import java.util.List;
import java.util.Optional;

@Repository
public interface DefectTypeRepository extends JpaRepository<DefectType, Long>, JpaSpecificationExecutor<DefectType> {
    @Query("select (count(d) > 0) from DefectType d where upper(d.name) = upper(?1) and d.deletedAt is null")
    boolean existsByNameIgnoreCaseAndDeletedAtNull(String name);

    @Query("""
            select (count(d) > 0) from DefectType d
            where upper(d.name) = upper(?1) and d.deletedAt is null and d.id <> ?2""")
    boolean existsByNameIgnoreCaseAndDeletedAtNullAndIdNot(String name, Long id);

    @Query("select d from DefectType d where d.id = ?1 and d.deletedAt is null")
    Optional<DefectType> findByIdAndDeletedAtNull(Long id);

    @Query("""
            select d from DefectType d inner join d.defectDetails defectDetails
            where d.deletedAt is null and defectDetails.workOrder.isActive = true""")
    List<DefectType> findByDeletedAtNullAndDefectDetails_WorkOrder_IsActiveTrue();

    @Query("""
            select d from DefectType d inner join d.defectDetails defectDetails
            where d.deletedAt is null and defectDetails.workOrder.isActive = true and defectDetails.workOrder.mo = ?1""")
    List<DefectType> findByDeletedAtNullAndDefectDetails_WorkOrder_IsActiveTrueAndDefectDetails_WorkOrder_Mo(String mo);


}
