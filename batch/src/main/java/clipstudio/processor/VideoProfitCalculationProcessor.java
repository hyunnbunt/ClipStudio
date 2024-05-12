package clipstudio.processor;

import clipstudio.dto.ProfitCalculator;
import clipstudio.dto.Video;
import clipstudio.dto.VideoDailyHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class VideoProfitCalculationProcessor implements ItemProcessor<Video, VideoDailyHistory> {
    @Override
    public VideoDailyHistory process(Video video) throws Exception {
        final Long prevTotal = video.getTotalViews();
        final Long daily = video.getTempDailyViews();
        double profit = ProfitCalculator.getDailyProfit(prevTotal, daily, "video");
        return VideoDailyHistory.builder()
                .videoNumber(video.getNumber())
                .updatedTotalViews(prevTotal+daily)
                .dailyProfit(profit)
                .dailyViews(daily)
                .calculatedDate(new Date()).build();
    }
}