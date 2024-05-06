package clipstudio.processor;

import clipstudio.dto.DailyProfitOfVideo;
import clipstudio.dto.DailyViews;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class VideoProfitCalculationProcessor implements ItemProcessor<DailyViews, DailyProfitOfVideo> {

    @Override
    public DailyProfitOfVideo process(DailyViews dailyViews) throws Exception {
        log.info(String.valueOf(dailyViews));
        final Long total = dailyViews.getTotalViews();
        final Long daily = dailyViews.getDailyViews();
        log.info("video total views: " + total);
        log.info("video daily views: " + daily);
        // should implement daily profit calculation code here.
        return DailyProfitOfVideo.builder()
                .videoNumber(dailyViews.getNumber())
                .dailyProfit(999L) // dummy data for testing.
                .calculatedDate(new Date()).build();
    }
}