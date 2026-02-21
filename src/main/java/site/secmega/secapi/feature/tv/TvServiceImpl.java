package site.secmega.secapi.feature.tv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.Tv;
import site.secmega.secapi.domain.TvData;
import site.secmega.secapi.feature.message.dto.MessageRequest;
import site.secmega.secapi.feature.tv.dto.*;
import site.secmega.secapi.mapper.TvMapper;
import site.secmega.secapi.util.AuthUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TvServiceImpl implements TvService{

    private final TvRepository tvRepository;
    private final TvMapper tvMapper;
    private final TvDataRepository tvDataRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final AuthUtil authUtil;

    @Override
    public TvDataResponse createDataTv(TvDataRequest tvDataRequest) {
        Tv tv = tvRepository.findByName(tvDataRequest.tvName()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tv not found")
        );
        tvMapper.updateTvFromTvDataRequest(tvDataRequest, tv);
        tv.setInput(tvDataRequest.input());
        tv.setWHour(tvDataRequest.wHour());
        tv.setHTarg(tvDataRequest.hTarg());
        Tv savedTv = tvRepository.save(tv);

        // Tv Data update
        TvData tvData = tvDataRepository.findByIsTodayTrueAndTv_Id(tv.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Today True not found!")
        );

        tvMapper.updateTvDataFromTvDataRequest(tvDataRequest, tvData);
        tvData.setDTarget(tvDataRequest.dTarg());

        tvDataRepository.save(tvData);

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
                .wHour(savedTv.getWHour())
                .hTarg(savedTv.getHTarg())
                .input(savedTv.getInput())
                .build();
    }

    @Override
    public TvDataResponse getDataByTvName(String name) {
        Tv tv = tvRepository.findByName(name).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tv not found!")
        );

        List<DailyRecord> dailyRecords = tv.getTvDatas().stream()
                .sorted(Comparator.comparing(TvData::getDate).reversed())
                .limit(3)
                .map(tvData -> DailyRecord.builder()
                        .h8(tvData.getH8())
                        .h9(tvData.getH9())
                        .h10(tvData.getH10())
                        .h11(tvData.getH11())
                        .h13(tvData.getH13())
                        .h14(tvData.getH14())
                        .h15(tvData.getH15())
                        .h16(tvData.getH16())
                        .h17(tvData.getH17())
                        .h18(tvData.getH18())
                        .date(tvData.getDate())
                        .dTarg(tvData.getDTarget())
                        .isToday(tvData.getIsToday())
                        .build()).toList();

        Defect defects = Defect.builder()
                .h8(tv.getDh8())
                .h9(tv.getDh9())
                .h10(tv.getDh10())
                .h11(tv.getDh11())
                .h13(tv.getDh13())
                .h14(tv.getDh14())
                .h15(tv.getDh15())
                .h16(tv.getDh16())
                .h17(tv.getDh17())
                .h18(tv.getDh18())
                .build();

        return TvDataResponse.builder()
                .line(tv.getLine())
                .worker(tv.getWorker())
                .orderNo(tv.getOrderNo())
                .totalInLine(tv.getTotalInLine())
                .balanceInLine(tv.getBalanceInLine())
                .orderQty(tv.getOrderQty())
                .totalOutput(tv.getTotalOutput())
                .qcRepairBack(tv.getQcRepairBack())
                .orderInline(tv.getOrderInline())
                .balanceDay(tv.getBalanceDay())
                .wHour(tv.getWHour())
                .hTarg(tv.getHTarg())
                .input(tv.getInput())
                .dailyRecords(dailyRecords)
                .defects(defects)
                .startDate(tv.getStartDate())
                .finishDate(tv.getFinishDate())
                .build();
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
        boolean isProdManager = authUtil.isManagerLoggedUser();
        List<Tv> tvs;
        if (isProdManager){
            tvs = tvRepository.findAll().stream().filter(tv -> !Objects.equals(tv.getName(), "General")).toList();
        }else {
            tvs = tvRepository.findAll();
        }
        return tvs.stream().map(tvMapper::toTvResponse).toList();
    }


}
