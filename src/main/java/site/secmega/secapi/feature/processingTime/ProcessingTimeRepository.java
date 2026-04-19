package site.secmega.secapi.feature.processingTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.ProcessingTime;

@Repository
public interface ProcessingTimeRepository extends JpaRepository<ProcessingTime, Long>, JpaSpecificationExecutor<ProcessingTime> {
    @Query("select (count(p) > 0) from ProcessingTime p where upper(p.style) = upper(?1) and p.deletedAt is null")
    boolean existsByStyleIgnoreCaseAndDeletedAtNull(String style);

}
