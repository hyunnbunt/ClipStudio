package clipstudio.processor;

import clipstudio.util.ProfitCalculator;
import clipstudio.dto.VideoDto;
import clipstudio.dto.VideoDailyProfitDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
public class VideoProfitCalculationProcessor implements ItemProcessor<VideoDto, VideoDto> {
    @Override
    public VideoDto process(VideoDto video) throws Exception {
        final Long prevTotal = video.getTotalViews();
        final Long daily = video.getTempDailyViews();
        log.info(String.valueOf(daily));
        double profit = ProfitCalculator.getDailyProfit(prevTotal, daily, "video");
        video.setTotalViews(prevTotal + daily);
        video.setDailyViews(daily);
        video.setTempDailyViews(0);
        video.setDailyProfit(profit);
        video.setCalculatedDate(LocalDate.now());
        return video;
    }
}