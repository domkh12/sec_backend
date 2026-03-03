package site.secmega.secapi.feature.productionLine;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.ProductionLine;

@Repository
public interface ProductionLineRepository extends JpaRepository<ProductionLine, Long> {

}
