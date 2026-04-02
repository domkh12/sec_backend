package site.secmega.secapi.feature.workOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.WorkOrder;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long>, JpaSpecificationExecutor<WorkOrder> {
    @Query("select (count(w) > 0) from WorkOrder w where upper(w.mo) = upper(?1)")
    boolean existsByMoIgnoreCase(String mo);

}
