package site.secmega.secapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private String category;
    private String size;
    private String color;
    private Integer targetProductionPerHour;
    private Double standardMinuteValue;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
