package site.secmega.secapi.feature.style;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.base.StyleStatus;
import site.secmega.secapi.domain.*;
import site.secmega.secapi.feature.style.dto.*;
import site.secmega.secapi.mapper.StyleMapper;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StyleServiceImpl implements StyleService {

    private final StyleRepository styleRepository;
    private final StyleMapper styleMapper;

    @Override
    public List<StyleLookupResponse> getStyleLookup() {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        List<Style> styles = styleRepository.findAll(sort);

        return styles.stream().map(styleMapper::toStyleLookupResponse).toList();
    }

    @Override
    public void deleteStyle(Long id) {
        Style style = styleRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Style not found")
        );
        style.setDeletedAt(LocalDateTime.now());
        styleRepository.save(style);
    }

    @Override
    public StyleStateResponse getStyleState() {
        List<Style> styles = styleRepository.findAll();

        long totalStyle = styles.size();

        long totalActive = styles.stream()
                .filter(style -> style.getPurchaseOrders().stream()
                        .flatMap(po -> po.getWorkOrders().stream())
                        .anyMatch(WorkOrder::getIsActive))
                .count();

        long totalDraft = totalStyle - totalActive;

        return StyleStateResponse.builder()
                .totalStyleNo(totalStyle)
                .totalActive(totalActive)
                .totalDraft(totalDraft)
                .build();
    }

    @Override
    public StyleResponse updateStyle(Long id, StyleRequest styleRequest) {
        Style style = styleRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Style not found")
        );

        styleMapper.updateFromStyleRequest(styleRequest, style);
        style.setUpdatedAt(LocalDateTime.now());
        Style updatedStyle = styleRepository.save(style);
        return styleMapper.toStyleResponse(updatedStyle);
    }

    @Override
    public StyleResponse createStyle(StyleRequest styleRequest) {

        if (styleRepository.existsByStyleNoIgnoreCaseAndDeletedAtNull(styleRequest.styleNo())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Style number already exist");
        }

        Style style = styleMapper.formStyleRequest(styleRequest);
        style.setStatus(StyleStatus.Draft);
        Style savedStyle = styleRepository.save(style);

        return styleMapper.toStyleResponse(savedStyle);
    }

    @Override
    public Page<StyleResponse> getStyle(StyleFilterRequest styleFilterRequest) {
        if (styleFilterRequest.pageNo() <= 0 || styleFilterRequest.pageSize() <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no and Page size must be greater than 0");
        }

        Specification<Style> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (styleFilterRequest.search() != null){
            String searchTerm = "%" + styleFilterRequest.search().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("styleNo")), searchTerm)
                    )
            );
        }

        if (styleFilterRequest.status() != null){
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("status"), styleFilterRequest.status())
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(styleFilterRequest.pageNo() - 1, styleFilterRequest.pageSize(), sort);
        Page<Style> styles = styleRepository.findAll(spec, pageRequest);
        return new PageImpl<>(toStyleResponse(styles), pageRequest, styles.getTotalElements());
    }

    private List<StyleResponse> toStyleResponse(Page<Style> styles){
        return styles.stream()
                .map(
                        style -> {
                            boolean active = style.getPurchaseOrders().stream()
                                    .flatMap(po -> po.getWorkOrders().stream())
                                    .anyMatch(WorkOrder::getIsActive);
                            return StyleResponse.builder()
                                    .id(style.getId())
                                    .styleNo(style.getStyleNo())
                                    .description(style.getDescription())
                                    .status(active ? StyleStatus.Active : StyleStatus.Draft)
                                    .build();
                        }
                ).toList();
    }
}
