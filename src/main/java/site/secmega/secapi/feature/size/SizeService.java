package site.secmega.secapi.feature.size;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.size.dto.SizeFilterRequest;
import site.secmega.secapi.feature.size.dto.SizeRequest;
import site.secmega.secapi.feature.size.dto.SizeResponse;

public interface SizeService {
    SizeResponse createService(@Valid SizeRequest sizeRequest);

    Page<SizeResponse> findAll(SizeFilterRequest sizeFilterRequest);

    SizeResponse updateSize(Long id, SizeRequest sizeRequest);

    void deleteSize(Long id);
}
