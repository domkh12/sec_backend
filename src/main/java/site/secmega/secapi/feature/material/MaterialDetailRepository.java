package site.secmega.secapi.feature.material;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.MaterialDetail;

import java.time.LocalDate;

@Repository
public interface MaterialDetailRepository extends JpaRepository<MaterialDetail, Long>, JpaSpecificationExecutor<MaterialDetail> {


}
