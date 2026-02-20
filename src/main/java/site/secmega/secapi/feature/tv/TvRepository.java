package site.secmega.secapi.feature.tv;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Tv;

import java.util.Optional;

@Repository
public interface TvRepository extends JpaRepository<Tv, Long> {
    @Query("select (count(t) > 0) from Tv t where t.name = ?1")
    boolean existsByName(String name);

    @Query("select t from Tv t where t.name = ?1")
    Optional<Tv> findByName(String name);


}
