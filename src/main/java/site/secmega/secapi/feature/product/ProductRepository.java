package site.secmega.secapi.feature.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select (count(p) > 0) from Product p where p.code = ?1 and p.deletedAt is null and p.id <> ?2")
    boolean existsByCodeAndDeletedAtNullAndIdNot(String code, Long id);

    @Query("select (count(p) > 0) from Product p where p.code = ?1")
    boolean existsByCode(String code);

    @Query("select (count(p) > 0) from Product p where p.code = ?1 and p.id <> ?2")
    boolean existsByCodeAndIdNot(String code, Long id);


}
