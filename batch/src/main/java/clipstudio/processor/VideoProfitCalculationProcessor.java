package clipstudio.processor;

import clipstudio.singleton.AdvertisementsProfitCache;
import clipstudio.util.ProfitCalculator;
import clipstudio.dto.VideoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class VideoProfitCalculationProcessor implements ItemProcessor<VideoDto, VideoDto> {
    final AdvertisementsProfitCache advertisementsProfitCache;
    @Override
    public VideoDto process(VideoDto video) throws Exception {
        final Long prevTotal = video.getTotalViews();
        final Long daily = video.getTempDailyViews();
        log.info(String.valueOf(daily));
        double profit = ProfitCalculator.getDailyProfit(prevTotal, daily, "video");
        video.setTotalViews(prevTotal + daily);
        video.setDailyViews(daily);
        video.setTempDailyViews(0);
        video.setDailyProfitOfVideo(profit);
        video.setCalculatedDate(LocalDate.now());
        final double dailyTotalProfitOfAdvertisements = advertisementsProfitCache.getAdProfitInVideo(video.getNumber());
        video.setDailyTotalProfitOfAdvertisements(dailyTotalProfitOfAdvertisements);
        return video;
    }
}