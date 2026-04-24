package site.secmega.secapi.feature.color;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.color.dto.ColorFilterRequest;
import site.secmega.secapi.feature.color.dto.ColorLookupResponse;
import site.secmega.secapi.feature.color.dto.ColorRequest;
import site.secmega.secapi.feature.color.dto.ColorResponse;

import java.util.List;

public interface ColorService {
    ColorResponse createColor(@Valid ColorRequest colorRequest);

    Page<ColorResponse> findAll(ColorFilterRequest colorFilterRequest);

    ColorResponse updateColor(Long id, @Valid ColorRequest colorRequest);

    void deleteColor(Long id);

    List<ColorLookupResponse> getColorLookup();
}
