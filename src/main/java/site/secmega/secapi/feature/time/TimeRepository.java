package site.secmega.secapi.feature.time;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Time;

@Repository
public interface TimeRepository extends JpaRepository<Time, Long> {
}
