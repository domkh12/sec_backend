package site.secmega.secapi.feature.materialColor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.secmega.secapi.domain.MaterialColor;

public interface MaterialColorRepository extends JpaRepository<MaterialColor, Long> {
    @Query("select (count(m) > 0) from material_colors m where upper(m.name) = upper(?1) and m.deletedAt is null")
    boolean existsByNameIgnoreCaseAndDeletedAtNull(String name);


}
