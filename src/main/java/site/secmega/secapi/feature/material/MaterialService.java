package site.secmega.secapi.feature.material;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.material.dto.MaterialFilterRequest;
import site.secmega.secapi.feature.material.dto.MaterialRequest;
import site.secmega.secapi.feature.material.dto.MaterialResponse;

import java.io.IOException;

public interface MaterialService {
    MaterialResponse createMaterial(@Valid MaterialRequest materialRequest) throws IOException;

    Page<MaterialResponse> findAll(MaterialFilterRequest materialFilterRequest);
}
