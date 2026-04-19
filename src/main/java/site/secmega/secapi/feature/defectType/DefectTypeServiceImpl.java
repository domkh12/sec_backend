package site.secmega.secapi.feature.defectType;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.DefectType;
import site.secmega.secapi.feature.defectType.dto.DefectTypeFilterRequest;
import site.secmega.secapi.feature.defectType.dto.DefectTypeRequest;
import site.secmega.secapi.feature.defectType.dto.DefectTypeResponse;
import site.secmega.secapi.mapper.DefectTypeMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DefectTypeServiceImpl implements DefectTypeService{

    private final DefectTypeRepository defectTypeRepository;
    private final DefectTypeMapper defectTypeMapper;

    @Override
    public Page<DefectTypeResponse> findAll(DefectTypeFilterRequest defectTypeFilterRequest) {
        if (defectTypeFilterRequest.pageNo() <= 0 || defectTypeFilterRequest.pageSize() <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no and Page size must be greater than 0");
        }

        Specification<DefectType> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (defectTypeFilterRequest.search() != null){
            String searchTerm = "%" + defectTypeFilterRequest.search().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), searchTerm)
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(defectTypeFilterRequest.pageNo() - 1, defectTypeFilterRequest.pageSize(), sort);
        Page<DefectType> defectTypes = defectTypeRepository.findAll(spec, pageRequest);

        return defectTypes.map(defectTypeMapper::toDefectTypeResponse);
    }

    @Override
    public DefectTypeResponse updateDefectType(Long id, DefectTypeRequest defectTypeRequest) {

        DefectType defectType = defectTypeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Defect type not found")
        );

        if (defectTypeRepository.existsByNameIgnoreCaseAndDeletedAtNullAndIdNot(defectTypeRequest.name(), id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Defect type name already exist");
        }

        defectTypeMapper.updateFromDefectTypeRequest(defectTypeRequest, defectType);
        defectType.setUpdatedAt(LocalDateTime.now());

        DefectType updatedDefectType = defectTypeRepository.save(defectType);

        return defectTypeMapper.toDefectTypeResponse(updatedDefectType);
    }

    @Override
    public void deleteDefectType(Long id) {
        DefectType defectType = defectTypeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Defect type not found")
        );
        defectType.setDeletedAt(LocalDateTime.now());
        defectTypeRepository.save(defectType);
    }

    @Override
    public DefectTypeResponse createDefectType(DefectTypeRequest defectTypeRequest) {

        if (defectTypeRepository.existsByNameIgnoreCaseAndDeletedAtNull(defectTypeRequest.name())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Defect type name already exist");
        }

        DefectType defectType = defectTypeMapper.fromDefectTypeRequest(defectTypeRequest);
        defectTypeRepository.save(defectType);

        return defectTypeMapper.toDefectTypeResponse(defectType);
    }
}
