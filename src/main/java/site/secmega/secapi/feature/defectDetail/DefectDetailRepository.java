package site.secmega.secapi.feature.defectDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.DefectDetail;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DefectDetailRepository extends JpaRepository<DefectDetail, Long>, JpaSpecificationExecutor<DefectDetail> {

    @Query("""
            select COALESCE(SUM(d.defectQty), 0) from DefectDetail d
            where d.deletedAt is null and d.defectDate = ?1 and d.workOrder.mo = ?2 and d.productionLine.id = ?3""")
    Integer totalDefectByMO(LocalDate defectDate, String mo, Long id);

    @Query("select COALESCE(SUM(d.defectQty), 0) from DefectDetail d where d.deletedAt is null and d.defectDate = ?1 and d.time.id = ?2")
    Integer totalDefectQtyTodayByTimeId(LocalDate defectDate, Long id);


    @Query("""
            select COALESCE(SUM(d.defectQty), 0) from DefectDetail d
            where d.deletedAt is null and d.defectDate = ?1 and d.workOrder.mo = ?2 and d.productionLine.id = ?3 and d.defectType.id = ?4""")
    Integer totalDefectByMoAndDefectTypeId(LocalDate defectDate, String mo, Long id, Long id1);

    @Query("""
            select
                d.defectDate as date,
                COALESCE(SUM(d.defectQty), 0) as defect
            from DefectDetail d
            where d.deletedAt is null
                and d.defectDate between ?1 and ?2
            group by d.defectDate
            order by d.defectDate asc
            """)
    List<Object[]> getDailyDefectSummaryBetweenDates(LocalDate dateFrom, LocalDate dateTo);

    @Query("select d from DefectDetail d where d.defectDate = ?1 and d.productionLine.line = ?2 and d.workOrder.mo = ?3")
    Optional<DefectDetail> findByDefectDateAndProductionLine_LineAndWorkOrder_Mo(LocalDate defectDate, String line, String mo);

    @Query("""
            select d from DefectDetail d
            where d.defectDate = ?1 and d.productionLine.line = ?2 and d.workOrder.purchaseOrder.style.id = ?3""")
    Optional<DefectDetail> findByDefectDateAndProductionLine_LineAndWorkOrder_PurchaseOrder_Style_Id(LocalDate defectDate, String line, Long id);

    @Query("""
            select COALESCE(SUM(d.defectQty), 0) from DefectDetail d
            where d.defectDate between ?1 and ?2 and d.productionLine.department.processNo = ?3 and d.time.id = ?4 and d.productionLine.id = ?5""")
    Integer sumDefectByLineAndTime(LocalDate defectDateStart, LocalDate defectDateEnd, Integer processNo, Long id, Long id1);


}
