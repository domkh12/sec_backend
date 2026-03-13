package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "file_metadata")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String storedName;
    String originalName;
    String contentType;
    Long size;

    // 👇 track who owns this file
    private String ownerType;   // "EMPLOYEE", "PRODUCT", "DOCUMENT"
    private Long ownerId;       // employee id, product id, etc.
    private boolean current;    // is this the active file?

    private LocalDateTime uploadedAt;
    private LocalDateTime replacedAt; // when it became old
}
