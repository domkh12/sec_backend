package site.secmega.secapi.feature.buyer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Buyer;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long>, JpaSpecificationExecutor<Buyer> {
    @Query("select count(b) from Buyer b")
    Integer countFirstBy();

    @Query("select (count(b) > 0) from Buyer b where upper(b.name) = upper(?1) and b.deletedAt is null")
    boolean existsByNameIgnoreCaseAndDeletedAtNull(String name);

    @Query("select (count(b) > 0) from Buyer b where upper(b.name) = upper(?1) and b.deletedAt is null and b.id <> ?2")
    boolean existsByNameIgnoreCaseAndDeletedAtNullAndIdNot(String name, Long id);


}
