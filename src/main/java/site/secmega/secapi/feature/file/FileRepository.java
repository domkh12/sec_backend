package site.secmega.secapi.feature.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.secmega.secapi.domain.FileMetadata;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileMetadata, Long> {
    @Query("select f from FileMetadata f where f.current = false and f.storedName = ''")
    List<FileMetadata> findByCurrentFalseAndStoredName();


    @Query("select f from FileMetadata f where f.current = false and f.storedName <> ''")
    List<FileMetadata> findByCurrentFalseAndStoredNameNot();


    @Query("select f from FileMetadata f where f.current = false")
    List<FileMetadata> findByCurrentFalse();

    @Query("select f from FileMetadata f where f.ownerId = ?1 and f.ownerType = ?2 and f.current = true")
    Optional<FileMetadata> findByOwnerIdAndOwnerTypeAndCurrentTrue(Long ownerId, String ownerType);


}
