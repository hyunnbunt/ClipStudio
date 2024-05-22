package clipstudio.processor;

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
import java.util.HashMap;

@Component
@Slf4j
@RequiredArgsConstructor
@StepScope
public class VideoProfitCalculationProcessor implements ItemProcessor<VideoDto, VideoDto> {
    final AdvertisementsProfitCache advertisementsProfitCache;

    @Value("#{jobParameters['batchDate']}")
    private String batchDate;
    @Override
    public VideoDto process(VideoDto video) throws Exception {
        log.info("Is thread virtual: " + Thread.currentThread().isVirtual());
        final Long prevTotal = video.getTotalViews();
        final Long daily = video.getTempDailyViews();
        log.info(String.valueOf(daily));
        double profit = ProfitCalculator.getDailyProfit(prevTotal, daily, "video");
        video.setTotalViews(prevTotal + daily);
        video.setDailyViews(daily);
        video.setTempDailyViews(0);
        video.setDailyProfitOfVideo(profit);
//        video.setCalculatedDate(LocalDate.now());
//        Thread.sleep(1000);
        log.info(batchDate);
        video.setCalculatedDate(LocalDate.parse(batchDate));
        final double dailyTotalProfitOfAdvertisements = advertisementsProfitCache.getAdProfitInVideo(video.getNumber());
        video.setDailyTotalProfitOfAdvertisements(dailyTotalProfitOfAdvertisements);
        return video;
    }
}