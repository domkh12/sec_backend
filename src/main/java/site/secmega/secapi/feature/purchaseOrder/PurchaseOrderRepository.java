package site.secmega.secapi.feature.purchaseOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.PurchaseOrder;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long>, JpaSpecificationExecutor<PurchaseOrder> {
    @Query("select (count(p) > 0) from PurchaseOrder p where upper(p.po) = upper(?1) and p.deletedAt is null")
    boolean existsByPoIgnoreCaseAndDeletedAtNull(String po);
}
