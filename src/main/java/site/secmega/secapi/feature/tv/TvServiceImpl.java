package site.secmega.secapi.feature.tv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.Tv;
import site.secmega.secapi.domain.TvData;
import site.secmega.secapi.feature.message.dto.MessageRequest;
import site.secmega.secapi.feature.tv.dto.TvDataRequest;
import site.secmega.secapi.feature.tv.dto.TvDataResponse;
import site.secmega.secapi.feature.tv.dto.TvRequest;
import site.secmega.secapi.feature.tv.dto.TvResponse;
import site.secmega.secapi.mapper.TvMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TvServiceImpl implements TvService{

    private final TvRepository tvRepository;
    private final TvMapper tvMapper;
    private final TvDataRepository tvDataRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public TvDataResponse createDataTv(TvDataRequest tvDataRequest) {
        Tv tv = tvRepository.findByName(tvDataRequest.tvName()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tv not found")
        );
        tvMapper.updateTvFromTvDataRequest(tvDataRequest, tv);

        Tv savedTv = tvRepository.save(tv);
//        TvData tvData = TvData.builder()
//                .
//                .build();
        messagingTemplate.convertAndSend("/topic/messages/tv-data-update", MessageRequest.builder()
                        .message("update")
                        .isUpdate(true)
                .build());

        return TvDataResponse.builder()
                .line(savedTv.getLine())
                .worker(savedTv.getWorker())
                .orderNo(savedTv.getOrderNo())
                .totalInLine(savedTv.getTotalInLine())
                .balanceInLine(savedTv.getBalanceInLine())
                .orderQty(savedTv.getOrderQty())
                .totalOutput(savedTv.getTotalOutput())
                .qcRepairBack(savedTv.getQcRepairBack())
                .orderInline(savedTv.getOrderInline())
                .balanceDay(savedTv.getBalanceDay())
                .build();
    }

    @Override
    public TvDataResponse getDataByTvName(String name) {
        Tv tv = tvRepository.findByName(name).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tv not found!")
        );

        return tvMapper.toTvDataResponse(tv);
    }

    @Override
    public TvResponse create(TvRequest tvRequest) {
        if (tvRepository.existsByName(tvRequest.name())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "TV name already exist");
        }
        Tv tv = tvMapper.fromTvRequest(tvRequest);
        tv.setCreatedAt(LocalDateTime.now());
        Tv savedTv = tvRepository.save(tv);
        return tvMapper.toTvResponse(savedTv);

    }

    @Override
    public List<TvResponse> findTv() {
        List<Tv> tvs = tvRepository.findAll();
        return tvs.stream().map(tvMapper::toTvResponse).toList();
    }


}
