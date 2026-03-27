package site.secmega.secapi.feature.color;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Color;

import java.util.Collection;
import java.util.List;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long>, JpaSpecificationExecutor<Color> {
    @Query("select (count(c) > 0) from Color c where upper(c.color) = upper(?1) and c.id <> ?2 and c.deletedAt is null")
    boolean existsByColorIgnoreCaseAndIdNotAndDeletedAtNull(String color, Long id);


    @Query("select (count(c) > 0) from Color c where upper(c.color) = upper(?1) and c.deletedAt is null")
    boolean existsByColorIgnoreCaseAndDeletedAtNull(String color);

    @Query("select c from Color c where c.id in ?1")
    List<Color> findByIdIn(Collection<Long> ids);


}
