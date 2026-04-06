package site.secmega.secapi.feature.material;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.base.MaterialStatus;
import site.secmega.secapi.domain.Material;
import site.secmega.secapi.feature.material.dto.MaterialFilterRequest;
import site.secmega.secapi.feature.material.dto.MaterialRequest;
import site.secmega.secapi.feature.material.dto.MaterialResponse;
import site.secmega.secapi.mapper.MaterialMapper;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService{

    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;

    @Override
    public Page<MaterialResponse> findAll(MaterialFilterRequest materialFilterRequest) {

        if (materialFilterRequest.pageNo() <= 0 || materialFilterRequest.pageSize() <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no and Page size must be greater than 0");
        }

        Specification<Material> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (materialFilterRequest.search() != null){
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("code")), "%" + materialFilterRequest.search().toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("name")), "%" + materialFilterRequest.search().toLowerCase() + "%")
                    ));
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(materialFilterRequest.pageNo() - 1, materialFilterRequest.pageSize(), sort);
        Page<Material> materials = materialRepository.findAll(spec, pageRequest);

        return materials.map(materialMapper::toMaterialResponse);
    }

    @Override
    public MaterialResponse createMaterial(MaterialRequest materialRequest) {

        if (materialRepository.existsByCodeAndDeletedAtNull(materialRequest.code())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Material code already exist");
        }

        if (materialRepository.existsByNameAndDeletedAtNull(materialRequest.name())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Material name already exist");
        }

        Material material = materialMapper.fromMaterialRequest(materialRequest);
        material.setStatus(MaterialStatus.LOW_STOCK);
        Material savedMaterial = materialRepository.save(material);

        return materialMapper.toMaterialResponse(savedMaterial);
    }
}
