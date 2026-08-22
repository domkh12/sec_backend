package site.secmega.secapi.feature.receipt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Receipt;

import java.util.Optional;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long>, JpaSpecificationExecutor<Receipt> {
    @Query("select (count(r) > 0) from receipts r where r.receiptNo = ?1 and r.deletedAt is null")
    boolean existsByReceiptNoAndDeletedAtNull(String receiptNo);

    @Query("select (count(r) > 0) from receipts r where r.receiptNo = ?1 and r.uuid <> ?2 and r.deletedAt is null")
    boolean existsByReceiptNoAndUuidNotAndDeletedAtNull(String receiptNo, String uuid);

    @Query("select r from receipts r where r.uuid = ?1 and r.deletedAt is null")
    Optional<Receipt> findByUuidAndDeletedAtNull(String uuid);


}
