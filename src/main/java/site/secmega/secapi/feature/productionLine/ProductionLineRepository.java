package site.secmega.secapi.feature.productionLine;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.ProductionLine;

import java.util.List;

@Repository
public interface ProductionLineRepository extends JpaRepository<ProductionLine, Long>, JpaSpecificationExecutor<ProductionLine> {
    @Query("select count(p) from ProductionLine p")
    Integer countLine();

    @Query("select p from ProductionLine p where p.department.id = ?1 order by p.line")
    List<ProductionLine> findByDepartment_IdOrderByLineAsc(Long id, Sort sort);

    @Query("select p from ProductionLine p where p.department.processNo = ?1 order by p.line")
    List<ProductionLine> findByDepartment_ProcessNoOrderByLineAsc(Integer processNo, Sort sort);


}
