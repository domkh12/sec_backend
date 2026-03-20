package site.secmega.secapi.feature.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    @Query("select (count(c) > 0) from Category c where upper(c.name) = upper(?1) and c.deletedAt is null")
    boolean existsByNameIgnoreCaseAndDeletedAtNull(String name);

}
