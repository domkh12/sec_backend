package site.secmega.secapi.feature.purchaseOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import site.secmega.secapi.base.POStatus;
import site.secmega.secapi.domain.PurchaseOrder;

import java.time.LocalDate;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long>, JpaSpecificationExecutor<PurchaseOrder> {
    @Query("select (count(p) > 0) from PurchaseOrder p where upper(p.po) = upper(?1) and p.deletedAt is null")
    boolean existsByPoIgnoreCaseAndDeletedAtNull(String po);

    @Query("""
            select (count(p) > 0) from PurchaseOrder p
            where upper(p.po) = upper(?1) and p.deletedAt is null and p.id <> ?2""")
    boolean existsByPoIgnoreCaseAndDeletedAtNullAndIdNot(String po, Long id);

    @Transactional
    @Modifying
    @Query("update PurchaseOrder p set p.status = ?1 where p.shipmentDate < ?2")
    int updateStatusByShipmentDateBefore(@NonNull POStatus status, LocalDate shipmentDate);



}
