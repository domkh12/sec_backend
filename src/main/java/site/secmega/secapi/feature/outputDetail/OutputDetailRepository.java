package site.secmega.secapi.feature.outputDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.OutputDetail;

@Repository
public interface OutputDetailRepository extends JpaRepository<OutputDetail, Long> {
    @Query("SELECT COALESCE(SUM(o.goodQty), 0) FROM OutputDetail o")
    long sumGoodOutputQty();

    @Query("SELECT COALESCE(SUM(o.goodQty), 0) FROM OutputDetail o WHERE o.workOrder.id = ?1")
    Integer sumGoodQtyByWorkOrder_Id(Long id);


}
