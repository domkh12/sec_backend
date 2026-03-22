package site.secmega.secapi.feature.buyer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Buyer;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {
}
