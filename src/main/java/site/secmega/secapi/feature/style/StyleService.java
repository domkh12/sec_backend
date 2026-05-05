package site.secmega.secapi.feature.style;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.style.dto.*;

public interface StyleService {

    Page<StyleResponse> getStyle(StyleFilterRequest styleFilterRequest);

    StyleResponse createStyle(@Valid StyleRequest styleRequest);

    StyleResponse updateStyle(Long id, StyleRequest styleRequest);

    void deleteStyle(Long id);

    StyleStateResponse getStyleState();
}
