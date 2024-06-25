package clipstudio.processor;

<<<<<<< HEAD
import clipstudio.service.CacheService;
import clipstudio.singleton.AdvertisementsProfitCache;
=======
import clipstudio.cache.AdvertisementsProfitCache;
>>>>>>> concurrentHashMap
import clipstudio.util.ProfitCalculator;
import clipstudio.dto.VideoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
@RequiredArgsConstructor
@StepScope
public class VideoProfitProcessor implements ItemProcessor<VideoDto, VideoDto> {
//    final AdvertisementsProfitCache advertisementsProfitCache;
    private final CacheService cacheService;
    @Value("#{jobParameters['batchDate']}")
    private String batchDate;
    @Override
    public VideoDto process(VideoDto videoDto) throws Exception {
<<<<<<< HEAD
        log.info("Inside profit processor: " + Thread.currentThread());
        log.info("Video number:" + videoDto.getNumber());
=======
//        log.info("Inside advertisement step: " + Thread.currentThread());
>>>>>>> concurrentHashMap
//        log.info("Is thread virtual: " + Thread.currentThread().isVirtual());
        final Long prevTotal = videoDto.getTotalViews();
        final Long todayViews = videoDto.getTodayViews();
        double profit = ProfitCalculator.getDailyProfit(prevTotal, todayViews, "video");
        videoDto.setTotalViews(prevTotal + todayViews);
        videoDto.setTodayViews(todayViews); // 테스트시 동일 데이터 2번 돌리기 위해 초기화하지 않고 진행
        videoDto.setVideoProfit(profit);
//        Thread.sleep(500);
<<<<<<< HEAD
        videoDto.setCalculatedDate(LocalDate.parse(batchDate));
        log.info("before getFromCache");
        synchronized (cacheService) {
            Double dailyTotalProfitOfAdvertisements = cacheService.getFromCache(videoDto.getNumber());
            if (dailyTotalProfitOfAdvertisements == null) {
                dailyTotalProfitOfAdvertisements = 0d;
            }
            videoDto.setDailyTotalProfitOfAdvertisements(dailyTotalProfitOfAdvertisements);
        }
        log.info("after getFromCache");
//                .getAdProfitInVideo(videoDto.getNumber()));
=======
        videoDto.setDate(LocalDate.parse(batchDate));
        videoDto.setAdvertisementsProfit(advertisementsProfitCache
                .getAdvertisementsProfit(videoDto.getNumber()));
>>>>>>> concurrentHashMap
        return videoDto;
    }
}