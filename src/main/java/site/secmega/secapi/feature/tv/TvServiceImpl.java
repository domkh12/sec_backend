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
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TvServiceImpl implements TvService{

    private final TvRepository tvRepository;
    private final TvMapper tvMapper;
    private final TvDataRepository tvDataRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final AuthUtil authUtil;



    @Override
    public List<TvGeneralResponse> getTvGeneralData() {
        Sort sort = Sort.by(Sort.Direction.ASC, "line");
        List<Tv> tvs = tvRepository.findByNameNotOrderByLineAsc("General", sort);
        LocalDate now = LocalDate.now();

        return tvs.stream()
                .map(tv -> {
                    LocalDate startDate = tv.getStartDate();
                    Long days = (startDate == null) ? null : ChronoUnit.DAYS.between(startDate, now);

                    List<TvData> sortedDatas = tv.getTvDatas().stream()
                            .sorted(Comparator.comparing(TvData::getDate).reversed())
                            .toList();

                    TvData latest    = sortedDatas.size() > 0 ? sortedDatas.get(0) : TvData.builder().build();
                    TvData yesterday = sortedDatas.size() > 1 ? sortedDatas.get(1) : TvData.builder().build();

                    int yesterdayTotal = Stream.of(
                            yesterday.getH8(), yesterday.getH9(), yesterday.getH10(), yesterday.getH11(),
                            yesterday.getH13(), yesterday.getH14(), yesterday.getH15(), yesterday.getH16(),
                            yesterday.getH17(), yesterday.getH18()
                    ).mapToInt(h -> h != null ? h : 0).sum();

                    int todayTotal = Stream.of(
                            latest.getH8(), latest.getH9(), latest.getH10(), latest.getH11(),
                            latest.getH13(), latest.getH14(), latest.getH15(), latest.getH16(),
                            latest.getH17(), latest.getH18()
                    ).mapToInt(h -> h != null ? h : 0).sum();

                    int todayDefectTotal = Stream.of(
                            latest.getDh8(), latest.getDh9(), latest.getDh10(), latest.getDh11(),
                            latest.getDh13(), latest.getDh14(), latest.getDh15(), latest.getDh16(),
                            latest.getDh17(), latest.getDh18()
                    ).mapToInt(h -> h != null ? h : 0).sum();

                    return TvGeneralResponse.builder()
                            .line(tv.getLine())
                            .styleNo(tv.getOrderNo())
                            .sewStart(startDate != null ? startDate.format(DateTimeFormatter.ofPattern("dd/MM")) : null)
                            .day(days)
                            .worker(tv.getWorker())
                            .act("0-0")
                            .hour(tv.getWHour())
                            .tarH(tv.getHTarg())
                            .tarDay(latest.getDTarget())
                            .h8(latest.getH8())
                            .h9(latest.getH9())
                            .h10(latest.getH10())
                            .h11(latest.getH11())
                            .h13(latest.getH13())
                            .h14(latest.getH14())
                            .h15(latest.getH15())
                            .h16(latest.getH16())
                            .h17(latest.getH17())
                            .h18(latest.getH18())
                            .finish(todayTotal)
                            .yFinish(yesterdayTotal)
                            .defects(todayDefectTotal)
                            .build();
                })
                .toList();
    }

    @Override
    public TvDataResponse createDataTv(String name) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Tv tv = tvRepository.findByName(name).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tv not found!")
        );

        boolean alreadyExists = tvDataRepository.existsByTvAndDate(tv, today);

        if (!alreadyExists) {
            TvData tvData = TvData.builder()
                    .date(today)
                    .isToday(true)
                    .tv(tv)
                    .build();

            tvDataRepository.save(tvData);

            List<TvData> sortedDatas = tv.getTvDatas().stream()
                    .sorted(Comparator.comparing(TvData::getDate).reversed())
                    .toList();

            TvData yesterday = sortedDatas.size() > 1 ? sortedDatas.get(1) : TvData.builder().build();
            yesterday.setIsToday(false);
            tvDataRepository.save(yesterday);
            messagingTemplate.convertAndSend("/topic/messages/tv-data-update", MessageRequest.builder()
                    .message("update")
                    .isUpdate(true)
                    .build());
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
                    .build();
        }else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Today data already exists");
        }
    }

    @Override
    public TvDataResponse updateDataTv(TvDataRequest tvDataRequest) {
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
        tvData.setDh8(tvDataRequest.dh8());
        tvData.setDh9(tvDataRequest.dh9());
        tvData.setDh10(tvDataRequest.dh10());
        tvData.setDh11(tvDataRequest.dh11());
        tvData.setDh13(tvDataRequest.dh13());
        tvData.setDh14(tvDataRequest.dh14());
        tvData.setDh15(tvDataRequest.dh15());
        tvData.setDh16(tvDataRequest.dh16());
        tvData.setDh17(tvDataRequest.dh17());
        tvData.setDh18(tvDataRequest.dh18());
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

        LocalDate now = LocalDate.now();
        LocalDate startDate = tv.getStartDate();
        Long days = (startDate == null) ? null : ChronoUnit.DAYS.between(startDate, now);

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

        List<TvData> sortedDatas = tv.getTvDatas().stream()
                .sorted(Comparator.comparing(TvData::getDate).reversed())
                .toList();

        TvData latest   = sortedDatas.size() > 0 ? sortedDatas.get(0) : TvData.builder().build();

        Defect defects = Defect.builder()
                .h8(latest.getDh8())
                .h9(latest.getDh9())
                .h10(latest.getDh10())
                .h11(latest.getDh11())
                .h13(latest.getDh13())
                .h14(latest.getDh14())
                .h15(latest.getDh15())
                .h16(latest.getDh16())
                .h17(latest.getDh17())
                .h18(latest.getDh18())
                .build();

        return TvDataResponse.builder()
                .line(tv.getLine())
                .worker(tv.getWorker())
                .day(days)
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
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        List<Tv> tvs;
        if (isProdManager){
            tvs = tvRepository.findAll(sort).stream().filter(tv -> !Objects.equals(tv.getName(), "General")).toList();
        }else {
            tvs = tvRepository.findAll(sort);
        }
        return tvs.stream().map(tvMapper::toTvResponse).toList();
    }


}
