package clipstudio.processor;

import clipstudio.util.ProfitCalculator;
import clipstudio.dto.AdvertisementDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class AdvertisementProfitCalculationProcessor implements ItemProcessor<AdvertisementDto, AdvertisementDto> {

    public final HashMap<Long, Double> dailyTotalProfitOfAdvertisementsCache;
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
        long videoNumber = advertisement.getVideo().getNumber();
        dailyTotalProfitOfAdvertisementsCache.put(videoNumber, dailyTotalProfitOfAdvertisementsCache.getOrDefault(videoNumber, 0d) + profit);
        return advertisement;
    }
}
