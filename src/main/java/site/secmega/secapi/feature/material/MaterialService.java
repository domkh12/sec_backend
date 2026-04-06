package site.secmega.secapi.feature.material;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.material.dto.MaterialFilterRequest;
import site.secmega.secapi.feature.material.dto.MaterialRequest;
import site.secmega.secapi.feature.material.dto.MaterialResponse;

public interface MaterialService {
    MaterialResponse createMaterial(@Valid MaterialRequest materialRequest);

    Page<MaterialResponse> findAll(MaterialFilterRequest materialFilterRequest);
}
