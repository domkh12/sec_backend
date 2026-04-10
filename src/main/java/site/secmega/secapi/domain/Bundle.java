package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.secmega.secapi.base.BundleStatus;

@Entity
@Table(name = "bundles")
@Getter
@Setter
@NoArgsConstructor
public class Bundle {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String qrCode;

    private Integer quantity; // Always 8 in your case
    private String size;      // e.g., "Large"
    private String color;     // e.g., "Navy Blue"

    @Enumerated(EnumType.STRING)
    private BundleStatus status;

    // Relationship
    @ManyToOne
    private Product product;
}
