package clipstudio.processor;

import clipstudio.util.ProfitCalculator;
import clipstudio.dto.AdvertisementDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
public class AdvertisementProfitCalculationProcessor implements ItemProcessor<AdvertisementDto, AdvertisementDto> {
    @Override
    public AdvertisementDto process(AdvertisementDto advertisement) throws Exception {
        final Long prevTotal = advertisement.getTotalViews();
        final Long daily = advertisement.getTempDailyViews();
        double profit = ProfitCalculator.getDailyProfit(prevTotal, daily, "advertisement");
        advertisement.setTotalViews(prevTotal + daily);
        advertisement.setDailyViews(daily);
        advertisement.setTempDailyViews(0);
        advertisement.setDailyProfit(profit);
        advertisement.setCalculatedDate(LocalDate.now());
        return advertisement;
    }
}
