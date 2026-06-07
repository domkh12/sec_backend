package site.secmega.secapi.feature.defectDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.DefectDetail;

@Repository
public interface DefectDetailRepository extends JpaRepository<DefectDetail, Long> {
}
