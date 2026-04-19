package site.secmega.secapi.feature.operation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.Operation;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long>, JpaSpecificationExecutor<Operation> {
}
