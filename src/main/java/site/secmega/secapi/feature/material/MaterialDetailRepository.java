package site.secmega.secapi.feature.material;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.base.TransactionType;
import site.secmega.secapi.domain.MaterialDetail;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaterialDetailRepository extends JpaRepository<MaterialDetail, Long>, JpaSpecificationExecutor<MaterialDetail> {
    @Query("select count(m.quantity) from MaterialDetail m where m.type = ?1")
    long countByType(TransactionType type);

    @Query("select m from MaterialDetail m where m.type = ?1")
    List<MaterialDetail> findByType(TransactionType type);

    @Query("select m from MaterialDetail m where m.type = ?1 order by m.id DESC")
    List<MaterialDetail> findByTypeOrderByIdDesc(TransactionType type, Sort sort);

    @Query("select m from MaterialDetail m where m.type = ?1 and m.material.id = ?2 order by m.id DESC")
    List<MaterialDetail> findByTypeAndMaterial_IdOrderByIdDesc(TransactionType type, Long id, Sort sort);


}
