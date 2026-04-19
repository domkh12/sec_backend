package site.secmega.secapi.feature.outputDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.OutputDetail;

@Repository
public interface OutputDetailRepository extends JpaRepository<OutputDetail, Long> {
}
