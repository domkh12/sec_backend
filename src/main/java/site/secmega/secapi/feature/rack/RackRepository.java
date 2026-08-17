package site.secmega.secapi.feature.rack;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Rack;

import java.util.Optional;

@Repository
public interface RackRepository extends JpaRepository<Rack, Long>, JpaSpecificationExecutor<Rack> {
    @Query("select (count(r) > 0) from racks r where r.code = ?1 and r.deletedAt is null")
    boolean existsByCodeAndDeletedAtNull(String code);

    @Query("select (count(r) > 0) from racks r where r.code = ?1 and r.deletedAt is null and r.uuid <> ?2")
    boolean existsByCodeAndDeletedAtNullAndUuidNot(String code, String uuid);

    @Query("select r from racks r where r.uuid = ?1 and r.deletedAt is null")
    Optional<Rack> findByUuidAndDeletedAtNull(String uuid);

    @Query("select r from racks r where r.uuid = ?1")
    Optional<Rack> findByUuid(String uuid);


}
