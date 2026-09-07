package site.secmega.secapi.feature.materialColor;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.MaterialColor;
import site.secmega.secapi.feature.materialColor.dto.MaterialColorFilterResponse;
import site.secmega.secapi.feature.materialColor.dto.MaterialColorRequest;
import site.secmega.secapi.feature.materialColor.dto.MaterialColorResponse;
import site.secmega.secapi.mapper.MaterialColorMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MaterialColorServiceImpl implements MaterialColorService{

    private final MaterialColorRepository materialColorRepository;
    private final MaterialColorMapper materialColorMapper;

    @Override
    public MaterialColorResponse updateMaterialColor(String uuid, MaterialColorRequest materialColorRequest) {




        return null;
    }

    @Override
    public MaterialColorResponse createMaterialColor(MaterialColorRequest materialColorRequest) {

        if (materialColorRepository.existsByNameIgnoreCaseAndDeletedAtNull(materialColorRequest.name())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Material color name already exist!");
        }

        MaterialColor materialColor = materialColorMapper.fromMaterialColorRequest(materialColorRequest);
        materialColor.setUuid(UUID.randomUUID().toString());
        MaterialColor savedMaterialColor = materialColorRepository.save(materialColor);
        return materialColorMapper.toMaterialColorResponse(savedMaterialColor);
    }

    @Override
    public Page<MaterialColorResponse> findAll(MaterialColorFilterResponse materialColorFilterResponse) {

        return null;
    }
}
