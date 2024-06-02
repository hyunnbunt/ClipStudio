package clipstudio.processor;

import clipstudio.service.CacheService;
import clipstudio.singleton.AdvertisementsProfitCache;
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
        log.info("Inside advertisement step: " + Thread.currentThread());
        log.info("Video number:" + videoDto.getNumber());
//        log.info("Is thread virtual: " + Thread.currentThread().isVirtual());
        final Long prevTotal = videoDto.getTotalViews();
        final Long todayViews = videoDto.getTodayViews();
        double profit = ProfitCalculator.getDailyProfit(prevTotal, todayViews, "video");
        videoDto.setTotalViews(prevTotal + todayViews);
        videoDto.setDailyViews(todayViews);
        videoDto.setTodayViews(todayViews); // multi, single thread 환경에서 동일 데이터 2번 돌리기 위해 초기화하지 않고 진행
        videoDto.setDailyProfitOfVideo(profit);
//        Thread.sleep(500);
        videoDto.setCalculatedDate(LocalDate.parse(batchDate));
        videoDto.setDailyTotalProfitOfAdvertisements(cacheService.getFromCache(videoDto.getNumber()));
//                .getAdProfitInVideo(videoDto.getNumber()));
        return videoDto;
    }
}