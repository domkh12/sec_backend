package site.secmega.secapi.feature.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
