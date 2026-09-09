package site.secmega.secapi.feature.materialColor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import site.secmega.secapi.domain.MaterialColor;

import java.util.Optional;

public interface MaterialColorRepository extends JpaRepository<MaterialColor, Long>, JpaSpecificationExecutor<MaterialColor> {

    @Query("select (count(m) > 0) from material_colors m where upper(m.name) = upper(?1) and m.deletedAt is null")
    boolean existsByNameIgnoreCaseAndDeletedAtNull(String name);

    @Query("select m from material_colors m where m.uuid = ?1")
    Optional<MaterialColor> findByUuid(String uuid);

    @Query("""
            select (count(m) > 0) from material_colors m
            where upper(m.name) = upper(?1) and m.deletedAt is null and m.uuid <> ?2""")
    boolean existsByNameIgnoreCaseAndDeletedAtNullAndUuidNot(String name, String uuid);


}
