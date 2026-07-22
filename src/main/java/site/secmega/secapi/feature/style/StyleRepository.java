package site.secmega.secapi.feature.style;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.base.StyleStatus;
import site.secmega.secapi.domain.Style;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface StyleRepository extends JpaRepository<Style, Long>, JpaSpecificationExecutor<Style> {

    @Query("select count(p) from Style p where p.status = ?1")
    long countByStatus(StyleStatus status);

    @Query("select (count(p) > 0) from Style p where upper(p.styleNo) = upper(?1) and p.deletedAt is null")
    boolean existsByStyleNoIgnoreCaseAndDeletedAtNull(String styleNo);

    @Query("select s from Style s where s.id in ?1 and s.deletedAt is null")
    List<Style> findByIdInAndDeletedAtNull(Collection<Long> ids);

    @Query("""
    select count(distinct s)
    from Style s
    join s.purchaseOrders po
    join po.workOrders wo
    join wo.outputDetails od
    where DATE(od.outputDate) = :date
    and s.deletedAt is null
    """)
    Integer countStylesWithOutputDetailsToday(@Param("date") LocalDate date);

    @Query("select s from Style s inner join s.purchaseOrders.workOrders workOrders where workOrders.mo = ?1")
    Optional<Style> findByPurchaseOrders_WorkOrders_Mo(String mo);


}
