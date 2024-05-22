package clipstudio.processor;

import clipstudio.singleton.AdvertisementsProfitCache;
import clipstudio.util.ProfitCalculator;
import clipstudio.dto.AdvertisementDto;
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
public class AdvertisementProfitCalculationProcessor implements ItemProcessor<AdvertisementDto, AdvertisementDto> {
    public final AdvertisementsProfitCache advertisementsProfitCache;

    @Value("#{jobParameters['batchDate']}")
    private String batchDate;
    @Override
    public AdvertisementDto process(AdvertisementDto advertisement) throws Exception {
        log.info("Thread: " + Thread.currentThread());
        advertisement.setCalculatedDate(LocalDate.parse(batchDate));
        final Long prevTotal = advertisement.getTotalViews();
        final Long daily = advertisement.getTempDailyViews();
        double profit = ProfitCalculator.getDailyProfit(prevTotal, daily, "advertisement");
        advertisement.setTotalViews(prevTotal + daily);
        advertisement.setDailyViews(daily);
        advertisement.setTempDailyViews(0);
        advertisement.setDailyProfit(profit);
//        advertisement.setCalculatedDate(LocalDate.now());
        log.info(batchDate);
        advertisement.setCalculatedDate(LocalDate.parse(batchDate));
        long videoNumber = advertisement.getVideoNumber();
        advertisementsProfitCache.addAdProfitInVideo(videoNumber, profit);
         return advertisement;
    }
}
