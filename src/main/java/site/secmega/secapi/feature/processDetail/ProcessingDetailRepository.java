package site.secmega.secapi.feature.processDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.ProcessingDetail;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcessingDetailRepository extends JpaRepository<ProcessingDetail, Long> {


}
