package site.secmega.secapi.feature.defectType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.DefectType;

@Repository
public interface DefectTypeRepository extends JpaRepository<DefectType, Long>, JpaSpecificationExecutor<DefectType> {
    @Query("select (count(d) > 0) from DefectType d where upper(d.name) = upper(?1) and d.deletedAt is null")
    boolean existsByNameIgnoreCaseAndDeletedAtNull(String name);

    @Query("""
            select (count(d) > 0) from DefectType d
            where upper(d.name) = upper(?1) and d.deletedAt is null and d.id <> ?2""")
    boolean existsByNameIgnoreCaseAndDeletedAtNullAndIdNot(String name, Long id);


}
