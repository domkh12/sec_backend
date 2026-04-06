package site.secmega.secapi.feature.material;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long>, JpaSpecificationExecutor<Material> {
    @Query("select (count(m) > 0) from Material m where m.code = ?1 and m.deletedAt is null")
    boolean existsByCodeAndDeletedAtNull(String code);

    @Query("select (count(m) > 0) from Material m where m.name = ?1 and m.deletedAt is null")
    boolean existsByNameAndDeletedAtNull(String name);


}
