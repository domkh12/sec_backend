package site.secmega.secapi.feature.tv;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.TvData;

@Repository
public interface TvDataRepository extends JpaRepository<TvData, Long> {


}
