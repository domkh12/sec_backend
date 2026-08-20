package site.secmega.secapi.feature.supplier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Supplier;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>, JpaSpecificationExecutor<Supplier> {
    @Query("select s from suppliers s where s.uuid = ?1")
    Optional<Supplier> findByUuid(String uuid);

    @Query("select (count(s) > 0) from suppliers s where s.supplierName = ?1 and s.deletedAt is null")
    boolean existsBySupplierNameAndDeletedAtNull(String supplierName);

    @Query("select (count(s) > 0) from suppliers s where s.supplierName = ?1 and s.deletedAt is null and s.uuid <> ?2")
    boolean existsBySupplierNameAndDeletedAtNullAndUuidNot(String supplierName, String uuid);


}
