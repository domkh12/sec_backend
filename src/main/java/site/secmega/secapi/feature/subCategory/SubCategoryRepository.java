package site.secmega.secapi.feature.subCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.SubCategory;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long>, JpaSpecificationExecutor<SubCategory> {

    @Query("select (count(s) > 0) from SubCategory s where upper(s.name) = upper(?1) and s.deletedAt is null")
    boolean existsByNameIgnoreCaseAndDeletedAtNull(String name);


}
