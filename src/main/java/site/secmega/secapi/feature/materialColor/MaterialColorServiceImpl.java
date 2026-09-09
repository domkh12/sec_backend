package site.secmega.secapi.feature.materialColor;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.MaterialColor;
import site.secmega.secapi.feature.materialColor.dto.MaterialColorFilterResponse;
import site.secmega.secapi.feature.materialColor.dto.MaterialColorRequest;
import site.secmega.secapi.feature.materialColor.dto.MaterialColorResponse;
import site.secmega.secapi.mapper.MaterialColorMapper;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MaterialColorServiceImpl implements MaterialColorService{

    private final MaterialColorRepository materialColorRepository;
    private final MaterialColorMapper materialColorMapper;

    @Override
    public void deleteMaterialColor(String uuid) {
        MaterialColor materialColor = materialColorRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material color not found!")
        );

        materialColor.setDeletedAt(LocalDateTime.now());
        materialColorRepository.save(materialColor);

    }

    @Override
    public MaterialColorResponse updateMaterialColor(String uuid, MaterialColorRequest materialColorRequest) {

        MaterialColor materialColor = materialColorRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material color not found!")
        );

        if (materialColorRepository.existsByNameIgnoreCaseAndDeletedAtNullAndUuidNot(materialColorRequest.name(), uuid)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Material color already exist!");
        }

        materialColorMapper.updateFromMaterialColorRequest(materialColorRequest, materialColor);
        MaterialColor updatedMaterialColor = materialColorRepository.save(materialColor);

        return materialColorMapper.toMaterialColorResponse(updatedMaterialColor);
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

        if (materialColorFilterResponse.pageNo() <= 0 || materialColorFilterResponse.pageSize() <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no and Page size invalid!");
        }

        Specification<MaterialColor> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (StringUtils.hasText(materialColorFilterResponse.search())){
            String searchTerm = "%" + materialColorFilterResponse.search().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("name")), searchTerm)
            ));
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(materialColorFilterResponse.pageNo() - 1, materialColorFilterResponse.pageSize(), sort);
        Page<MaterialColor> materialColors = materialColorRepository.findAll(spec, pageRequest);

        return materialColors.map(materialColorMapper::toMaterialColorResponse);
    }
}
