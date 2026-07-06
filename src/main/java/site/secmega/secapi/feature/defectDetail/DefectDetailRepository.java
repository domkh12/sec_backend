package site.secmega.secapi.feature.defectDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.DefectDetail;

import java.time.LocalDate;

@Repository
public interface DefectDetailRepository extends JpaRepository<DefectDetail, Long> {
    @Query("""
            select COALESCE(SUM(d.defectQty), 0) from DefectDetail d
            where d.deletedAt is null and d.defectDate = ?1 and d.workOrder.mo = ?2 and d.productionLine.department.processNo = ?3""")
    Integer totalDefectByMO(LocalDate defectDate, String mo, Integer processNo);


}
