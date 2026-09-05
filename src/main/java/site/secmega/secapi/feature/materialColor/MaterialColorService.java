package site.secmega.secapi.feature.materialColor;

import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.materialColor.dto.MaterialColorFilterResponse;
import site.secmega.secapi.feature.materialColor.dto.MaterialColorRequest;
import site.secmega.secapi.feature.materialColor.dto.MaterialColorResponse;

public interface MaterialColorService {
    MaterialColorResponse createMaterialColor(MaterialColorRequest materialColorRequest);

    Page<MaterialColorResponse> findAll(MaterialColorFilterResponse materialColorFilterResponse);
}
