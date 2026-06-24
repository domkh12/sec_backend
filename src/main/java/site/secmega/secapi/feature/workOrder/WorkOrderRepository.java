package site.secmega.secapi.feature.workOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.base.WorkOrderStatus;
import site.secmega.secapi.domain.WorkOrder;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long>, JpaSpecificationExecutor<WorkOrder> {
    @Query("select (count(w) > 0) from WorkOrder w where upper(w.mo) = upper(?1)")
    boolean existsByMoIgnoreCase(String mo);

    @Query("select w from WorkOrder w where w.mo = ?1")
    Optional<WorkOrder> findByMo(String mo);

    @Query("select count(w) from WorkOrder w")
    long countFirstBy();

    @Query("SELECT COALESCE(SUM(w.qty), 0) from WorkOrder w where w.deletedAt is null")
    long countByDeletedAtNull();


    @Query("select (count(w) > 0) from WorkOrder w where upper(w.mo) = upper(?1) and w.deletedAt is null and w.id <> ?2")
    boolean existsByMoIgnoreCaseAndDeletedAtNullAndIdNot(String mo, Long id);

    @Query("""
            select w from WorkOrder w inner join w.productionLines productionLines
            where productionLines.id = ?1 and w.deletedAt is null""")
    List<WorkOrder> findByProductionLines_IdAndDeletedAtNull(Long id);

    @Query("""
            select w from WorkOrder w inner join w.productionLines productionLines
            where productionLines.id = ?1 and w.deletedAt is null and w.isActive = true""")
    List<WorkOrder> findByProductionLines_IdAndDeletedAtNullAndIsActiveTrue(Long id);

    @Query("select w from WorkOrder w where w.isActive = ?1")
    List<WorkOrder> findByIsActive(Boolean isActive);

    @Query("select COALESCE(SUM(w.qty), 0) from WorkOrder w where w.isActive = true")
    Integer sumByIsActiveTrue();



}
