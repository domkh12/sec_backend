package site.secmega.secapi.feature.productColor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.ProductSku;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductColorRepository extends JpaRepository<ProductSku, Long> {

    @Query("select p from ProductSku p where p.product.id = ?1 and p.color.id in ?2")
    List<ProductSku> findByProductIdAndColorIds(Long id, Collection<Long> ids);


}
