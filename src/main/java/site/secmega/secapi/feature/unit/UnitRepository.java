package site.secmega.secapi.feature.unit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Unit;

import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long>, JpaSpecificationExecutor<Unit> {
    @Query("select (count(u) > 0) from units u where u.unitCode = ?1 and u.deletedAt is null")
    boolean existsByUnitCodeAndDeletedAtNull(String unitCode);

    @Query("select (count(u) > 0) from units u where u.unitName = ?1 and u.deletedAt is null")
    boolean existsByUnitNameAndDeletedAtNull(String unitName);

    Optional<Unit> findByUuid(String uuid);
}
