package clipstudio.processor;

import clipstudio.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class AdvertisementProfitCalculationProcessor implements ItemProcessor<Advertisement, AdvertisementDailyHistory> {
    @Override
    public AdvertisementDailyHistory process(Advertisement advertisement) throws Exception {
        final Long prevTotal = advertisement.getTotalViews();
        final Long daily = advertisement.getTempDailyViews();
        double profit = ProfitCalculator.getDailyProfit(prevTotal, daily, "advertisement");
        return AdvertisementDailyHistory.builder()
                .advertisementNumber(advertisement.getNumber())
                .updatedTotalViews(prevTotal + daily)
                .dailyProfit(profit)
                .dailyViews(daily)
                .calculatedDate(new Date()).build();
    }
}
