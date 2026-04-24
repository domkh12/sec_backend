package site.secmega.secapi.feature.color;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.Color;
import site.secmega.secapi.feature.color.dto.ColorFilterRequest;
import site.secmega.secapi.feature.color.dto.ColorLookupResponse;
import site.secmega.secapi.feature.color.dto.ColorRequest;
import site.secmega.secapi.feature.color.dto.ColorResponse;
import site.secmega.secapi.mapper.ColorMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService{

    private final ColorRepository colorRepository;
    private final ColorMapper colorMapper;

    @Override
    public void deleteColor(Long id) {
        Color color = colorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Color not found")
        );
        color.setDeletedAt(LocalDateTime.now());
        colorRepository.save(color);
    }

    @Override
    public List<ColorLookupResponse> getColorLookup() {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        List<Color> colors = colorRepository.findAll(sort);

        return colors.stream().map(colorMapper::toColorLookupResponse).toList();
    }

    @Override
    public ColorResponse updateColor(Long id, ColorRequest colorRequest) {
        Color color = colorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Color not found")
        );

        if (colorRepository.existsByColorIgnoreCaseAndIdNotAndDeletedAtNull(colorRequest.color(), id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Color already exist");
        }

        colorMapper.updateFromColorRequest(colorRequest, color);
        color.setUpdatedAt(LocalDateTime.now());
        Color updatedColor = colorRepository.save(color);
        return colorMapper.toColorResponse(updatedColor);
    }

    @Override
    public Page<ColorResponse> findAll(ColorFilterRequest colorFilterRequest) {
        if (colorFilterRequest.pageNo() <= 0 || colorFilterRequest.pageSize() <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no and Page size must be greater than 0");
        }

        Specification<Color> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (colorFilterRequest.search() != null){
            String searchTerm = "%" + colorFilterRequest.search().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("color")), searchTerm)
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(colorFilterRequest.pageNo() - 1, colorFilterRequest.pageSize(), sort);
        Page<Color> colors = colorRepository.findAll(spec, pageRequest);

        return colors.map(colorMapper::toColorResponse);
    }

    @Override
    public ColorResponse createColor(ColorRequest colorRequest) {
        if (colorRepository.existsByColorIgnoreCaseAndDeletedAtNull(colorRequest.color())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Color already exist");
        }

        Color color = colorMapper.fromColorRequest(colorRequest);
        Color savedColor = colorRepository.save(color);
        return colorMapper.toColorResponse(savedColor);
    }

}
