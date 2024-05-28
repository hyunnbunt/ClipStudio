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

@Component
@Slf4j
@RequiredArgsConstructor
@StepScope
public class AdvertisementProfitProcessor implements ItemProcessor<AdvertisementDto, AdvertisementDto> {
    public final AdvertisementsProfitCache advertisementsProfitCache;

    @Value("#{jobParameters['batchDate']}")
    private String batchDate;
    @Override
    public AdvertisementDto process(AdvertisementDto advertisementDto) throws Exception {
        log.info("Inside advertisementDto step: " + Thread.currentThread());
        log.info("Advertisement number:" + advertisementDto.getNumber());
        advertisementDto.setCalculatedDate(LocalDate.parse(batchDate));
        final Long prevTotal = advertisementDto.getTotalViews();
        final Long daily = advertisementDto.getTodayViews();
        double profit = ProfitCalculator.getDailyProfit(prevTotal, daily, "advertisement");
        advertisementDto.setTotalViews(prevTotal + daily);
        advertisementDto.setDailyViews(daily);
        advertisementDto.setTodayViews(0);
        advertisementDto.setDailyProfit(profit);
//        advertisementDto.setCalculatedDate(LocalDate.now());
//        Thread.sleep(10);
        log.info(batchDate);
        advertisementDto.setCalculatedDate(LocalDate.parse(batchDate));
        long videoNumber = advertisementDto.getVideoNumber();
        advertisementsProfitCache.addAdProfitInVideo(videoNumber, profit);
         return advertisementDto;
    }
}
