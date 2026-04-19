package site.secmega.secapi.feature.size;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.Size;
import site.secmega.secapi.feature.size.dto.SizeFilterRequest;
import site.secmega.secapi.feature.size.dto.SizeLookupResponse;
import site.secmega.secapi.feature.size.dto.SizeRequest;
import site.secmega.secapi.feature.size.dto.SizeResponse;
import site.secmega.secapi.mapper.SizeMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService{

    private final SizeRepository sizeRepository;
    private final SizeMapper sizeMapper;

    @Override
    public void deleteSize(Long id) {
        Size size = sizeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Size not found")
        );
        size.setDeletedAt(LocalDateTime.now());
        sizeRepository.save(size);
    }

    @Override
    public List<SizeLookupResponse> getSizeLookup() {
        List<Size> sizes = sizeRepository.findAll();
        return sizes.stream().map(size -> SizeLookupResponse.builder()
                .id(size.getId())
                .size(size.getSize())
                .build()).toList();
    }

    @Override
    public SizeResponse updateSize(Long id, SizeRequest sizeRequest) {
        Size size = sizeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Size not found")
        );

        if (sizeRepository.existsBySizeIgnoreCaseAndIdNotAndDeletedAtNull(sizeRequest.size(), id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Size already exist");
        }

        sizeMapper.updateFromSizeRequest(sizeRequest, size);
        size.setUpdatedAt(LocalDateTime.now());
        Size updatedSize = sizeRepository.save(size);

        return sizeMapper.toSizeResponse(updatedSize);
    }

    @Override
    public Page<SizeResponse> findAll(SizeFilterRequest sizeFilterRequest) {
        if (sizeFilterRequest.pageNo() <= 0 || sizeFilterRequest.pageSize() <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no and Page size must be greater than 0");
        }

        Specification<Size> spec = Specification.where((root, query, cb) -> cb.conjunction());
        if (sizeFilterRequest.search() != null){
            String searchTerm = "%" + sizeFilterRequest.search().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("size")), searchTerm)
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(sizeFilterRequest.pageNo() - 1, sizeFilterRequest.pageSize(), sort);
        Page<Size> sizes = sizeRepository.findAll(spec, pageRequest);

        return sizes.map(sizeMapper::toSizeResponse);
    }

    @Override
    public SizeResponse createService(SizeRequest sizeRequest) {
        if (sizeRepository.existsBySizeIgnoreCaseAndDeletedAtNull(sizeRequest.size())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Size already exist");
        }
        Size size = sizeMapper.fromSizeRequest(sizeRequest);
        Size savedSize = sizeRepository.save(size);
        return sizeMapper.toSizeResponse(savedSize);
    }
}
