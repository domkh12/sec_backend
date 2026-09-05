package site.secmega.secapi.feature.materialColor;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import site.secmega.secapi.feature.materialColor.dto.MaterialColorFilterResponse;
import site.secmega.secapi.feature.materialColor.dto.MaterialColorRequest;
import site.secmega.secapi.feature.materialColor.dto.MaterialColorResponse;

@Service
public class MaterialColorServiceImpl implements MaterialColorService{
    @Override
    public MaterialColorResponse createMaterialColor(MaterialColorRequest materialColorRequest) {




        return null;
    }

    @Override
    public Page<MaterialColorResponse> findAll(MaterialColorFilterResponse materialColorFilterResponse) {

        return null;
    }
}
