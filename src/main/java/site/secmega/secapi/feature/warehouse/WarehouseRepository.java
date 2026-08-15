package site.secmega.secapi.feature.warehouse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Warehouse;

import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long>, JpaSpecificationExecutor<Warehouse> {
    @Query("select (count(w) > 0) from warehouses w where w.code = ?1")
    boolean existsByCode(String code);

    @Query("select (count(w) > 0) from warehouses w where w.name = ?1")
    boolean existsByName(String name);

    @Query("select w from warehouses w where w.uuid = ?1")
    Optional<Warehouse> findByUuid(String uuid);

    @Query("select (count(w) > 0) from warehouses w where w.code = ?1 and w.uuid <> ?2")
    boolean existsByCodeAndUuidNot(String code, String uuid);

    @Query("select (count(w) > 0) from warehouses w where w.name = ?1 and w.uuid <> ?2")
    boolean existsByNameAndUuidNot(String name, String uuid);


}
