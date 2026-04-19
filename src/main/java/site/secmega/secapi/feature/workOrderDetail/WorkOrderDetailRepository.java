package site.secmega.secapi.feature.workOrderDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.WorkOrderDetail;

@Repository
public interface WorkOrderDetailRepository extends JpaRepository<WorkOrderDetail, Long> {
}
