package site.secmega.secapi.feature.processingTime;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.Operation;
import site.secmega.secapi.domain.ProcessingDetail;
import site.secmega.secapi.domain.ProcessingTime;
import site.secmega.secapi.feature.operation.OperationRepository;
import site.secmega.secapi.feature.operation.dto.OperationResponse;
import site.secmega.secapi.feature.processDetail.ProcessingDetailRepository;
import site.secmega.secapi.feature.processingTime.dto.*;
import site.secmega.secapi.mapper.ProcessingTimeMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProcessingTimeServiceImpl implements ProcessingTimeService{

    private final ProcessingTimeRepository processingTimeRepository;
    private final ProcessingTimeMapper processingTimeMapper;
    private final OperationRepository operationRepository;
//    private final ProcessingDetailRepository processingDetailRepository;

    @Override
    public List<ProcessingTimeLookupResponse> findProcessingTimeLookup() {
        List<ProcessingTime> processingTimes = processingTimeRepository.findAll();
        return processingTimes.stream().map(pt -> ProcessingTimeLookupResponse.builder()
                .id(pt.getId())
                .style(pt.getStyle())
                .build()).toList();
    }

    @Override
    public Page<ProcessingTimeResponse> findAll(ProcessingTimeFilterRequest processingTimeFilterRequest) {
        if (processingTimeFilterRequest.pageNo() <= 0 || processingTimeFilterRequest.pageSize() <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no and Page size must be greater than 0");
        }

        Specification<ProcessingTime> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (processingTimeFilterRequest.search() != null){
            String searchTerm = "%" + processingTimeFilterRequest.search().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("style")), searchTerm)
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(processingTimeFilterRequest.pageNo() - 1, processingTimeFilterRequest.pageSize(), sort);
        Page<ProcessingTime> processingTimes = processingTimeRepository.findAll(spec, pageRequest);

        return new PageImpl<>(toProcessingTimeResponsePage(processingTimes), pageRequest, processingTimes.getTotalElements());
    }

    @Override
    public ProcessingTimeResponse createProcessingTime(ProcessingTimeRequest processingTimeRequest) {

        if (processingTimeRepository.existsByStyleIgnoreCaseAndDeletedAtNull(processingTimeRequest.style())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Processing time style already exist");
        }

        ProcessingTime processingTime = processingTimeMapper.fromProcessingTimeRequest(processingTimeRequest);

        ProcessingTime savedProcessingTime = processingTimeRepository.save(processingTime);
        if (processingTimeRequest.operation() != null && !processingTimeRequest.operation().isEmpty()){
            processingTimeRequest.operation().forEach(op -> {
                Operation operation = operationRepository.findById(op.operationId()).orElseThrow();
                ProcessingDetail processingDetail = new ProcessingDetail();
                processingDetail.setOrderingNumber(op.orderingNumber());
//                processingDetail.setOperation(operation);
//                processingDetail.setProcessingTime(savedProcessingTime);
//                ProcessingDetail savedProcessingDetail = processingDetailRepository.save(processingDetail);
//                savedProcessingTime.getProcessingDetails().add(savedProcessingDetail);
            });
        }

        return toProcessingTimeResponse(savedProcessingTime);
    }

    private List<ProcessingTimeResponse> toProcessingTimeResponsePage(Page<ProcessingTime> processingTimes){
        return processingTimes.map(pt -> ProcessingTimeResponse.builder()
                .id(pt.getId())
                .style(pt.getStyle())
                .createdAt(pt.getCreatedAt())
                .updatedAt(pt.getUpdatedAt())
//                .operation(pt.getProcessingDetails().stream()
////                        .filter(ptd -> ptd.getProcessingTime().equals(pt))
////                        .map(ptd -> ProcessOperationResponse.builder()
////                                .operationId(ptd.getOperation().getId())
////                                .orderingNumber(ptd.getOrderingNumber())
////                                .operationName(ptd.getOperation().getName())
////                                .build()).toList()
//                )
                .build()).toList();
    }

    private ProcessingTimeResponse toProcessingTimeResponse(ProcessingTime processingTime){
        return ProcessingTimeResponse.builder()
                .id(processingTime.getId())
                .style(processingTime.getStyle())
                .createdAt(processingTime.getCreatedAt())
                .updatedAt(processingTime.getUpdatedAt())
//                .operation(processingTime.getProcessingDetails().stream()
//                        .filter(ptd -> ptd.getProcessingTime().equals(processingTime))
//                        .map(ptd -> ProcessOperationResponse.builder()
//                                .operationId(ptd.getOperation().getId())
//                                .orderingNumber(ptd.getOrderingNumber())
//                                .operationName(ptd.getOperation().getName())
//                                .build()).toList()
//                )
                .build();
    }
}
