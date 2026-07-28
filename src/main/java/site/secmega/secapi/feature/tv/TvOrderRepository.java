package site.secmega.secapi.feature.tv;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.TvOrder;

import java.util.List;

@Repository
public interface TvOrderRepository extends JpaRepository<TvOrder, Long> {
    @Query("select t from TvOrder t where t.tv.name = ?1")
    List<TvOrder> findByTv_Name(String name);

    @Query("select (count(t) > 0) from TvOrder t where t.tv.id = ?1 and t.style.id = ?2")
    boolean existsByTv_IdAndStyle_Id(Long id, Long id1);


}
