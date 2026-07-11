package site.secmega.secapi.feature.tv;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Tv;
import site.secmega.secapi.domain.TvData;

import java.util.List;
import java.util.Optional;

@Repository
public interface TvDataRepository extends JpaRepository<TvData, Long> {
//    boolean existsByTvAndDate(Tv tv, String date);
//
//    @Query("select t from TvData t where t.date = ?1")
//    Optional<TvData> findByDate(String date);
//
//    @Query("select t from TvData t where t.date = ?1 and t.tv.name = ?2")
//    Optional<TvData> findByDateAndTv_Name(String date, String name);
//
//
//    @Query("select t from TvData t where t.isToday = true")
//    Optional<TvData> findByIsTodayTrue();
//
//    @Query("select t from TvData t where t.isToday = true and t.tv.id = ?1")
//    Optional<TvData> findByIsTodayTrueAndTv_Id(Long id);
//
//    @Modifying
//    @Transactional
//    @Query("UPDATE TvData t SET t.isToday = false WHERE t.tv = :tv")
//    void clearIsTodayByTv(@Param("tv") Tv tv);
}
