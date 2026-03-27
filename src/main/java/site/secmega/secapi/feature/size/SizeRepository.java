package site.secmega.secapi.feature.size;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Size;

import java.util.Collection;
import java.util.List;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long>, JpaSpecificationExecutor<Size> {
    @Query("select (count(s) > 0) from Size s where upper(s.size) = upper(?1) and s.id <> ?2 and s.deletedAt is null")
    boolean existsBySizeIgnoreCaseAndIdNotAndDeletedAtNull(String size, Long id);


    @Query("select (count(s) > 0) from Size s where upper(s.size) = upper(?1) and s.deletedAt is null")
    boolean existsBySizeIgnoreCaseAndDeletedAtNull(String size);

    @Query("select s from Size s where s.id in ?1")
    List<Size> findByIdIn(Collection<Long> ids);


}
