package site.secmega.secapi.feature.time;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Time;

import java.util.List;

@Repository
public interface TimeRepository extends JpaRepository<Time, Long> {
    @Query("select t from Time t where t.deletedAt is null")
    List<Time> findByDeletedAtNull();

}
