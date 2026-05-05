package site.secmega.secapi.feature.style;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.base.StyleStatus;
import site.secmega.secapi.domain.Style;

@Repository
public interface StyleRepository extends JpaRepository<Style, Long>, JpaSpecificationExecutor<Style> {

    @Query("select count(p) from Style p where p.status = ?1")
    long countByStatus(StyleStatus status);

    @Query("select (count(p) > 0) from Style p where upper(p.styleNo) = upper(?1) and p.deletedAt is null")
    boolean existsByStyleNoIgnoreCaseAndDeletedAtNull(String styleNo);

}
