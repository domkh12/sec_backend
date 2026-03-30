package site.secmega.secapi.feature.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.base.ProductStatus;
import site.secmega.secapi.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("select count(p) from Product p where p.status = ?1")
    long countByStatus(ProductStatus status);

    @Query("select (count(p) > 0) from Product p where upper(p.styleNo) = upper(?1) and p.deletedAt is null")
    boolean existsByStyleNoIgnoreCaseAndDeletedAtNull(String styleNo);

}
