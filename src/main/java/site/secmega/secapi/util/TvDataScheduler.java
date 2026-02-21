package site.secmega.secapi.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import site.secmega.secapi.domain.Tv;
import site.secmega.secapi.domain.TvData;
import site.secmega.secapi.feature.tv.TvDataRepository;
import site.secmega.secapi.feature.tv.TvRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TvDataScheduler {
    private final TvDataRepository tvDataRepository;
    private final TvRepository tvRepository;

    // Runs every day at midnight (00:00:00)
    @Scheduled(cron = "0 0 0 * * *")
    public void createDailyTvData() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        List<Tv> allTvs = tvRepository.findAll();

        for (Tv tv : allTvs) {
            // Prevent duplicate creation if already exists for today
            boolean alreadyExists = tvDataRepository.existsByTvAndDate(tv, today);

            if (!alreadyExists) {
                TvData tvData = TvData.builder()
                        .date(today)
                        .isToday(true)
                        .tv(tv)
                        .actualOutput(0)
                        .defectQty(0)
                        .finish(0)
                        .yesterdayFinish(0)
                        .finishPercentage(0.0)
                        .defectPercentage(0.0)
                        .dTarget(0)
                        .nowTarget(0)
                        .h8(0).h9(0).h10(0).h11(0)
                        .h13(0).h14(0).h15(0).h16(0).h17(0).h18(0)
                        .build();

                tvDataRepository.save(tvData);
                log.info("Created TvData for TV: {} on date: {}", tv.getId(), today);
            }
        }
    }

    // Also reset isToday flag for yesterday's records
    @Scheduled(cron = "0 0 0 * * *")
    public void resetYesterdayIsToday() {
        String yesterday = LocalDate.now().minusDays(1)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        List<TvData> yesterdayRecords = tvDataRepository.findAllByDate(yesterday);
        for (TvData record : yesterdayRecords) {
            record.setIsToday(false);
            tvDataRepository.save(record);
        }

        log.info("Reset isToday=false for date: {}", yesterday);
    }
}
