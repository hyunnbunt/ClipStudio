package clipstudio.processor;

import clipstudio.service.CacheService;
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
//    public final AdvertisementsProfitCache advertisementsProfitCache;
    private final CacheService cacheService;
    @Value("#{jobParameters['batchDate']}")
    private String batchDate;
    @Override
    public AdvertisementDto process(AdvertisementDto advertisementDto) throws Exception {
        log.info("Inside advertisementDto step: " + Thread.currentThread());
        log.info("Advertisement number:" + advertisementDto.getNumber());
        advertisementDto.setCalculatedDate(LocalDate.parse(batchDate));
        final Long prevTotal = advertisementDto.getTotalViews();
        final Long todayViews = advertisementDto.getTodayViews();
        double profit = ProfitCalculator.getDailyProfit(prevTotal, todayViews, "advertisement");
        advertisementDto.setTotalViews(prevTotal + todayViews);
        advertisementDto.setDailyViews(todayViews);
        advertisementDto.setTodayViews(todayViews); // multi, single thread 환경에서 동일 데이터 2번 돌리기 위해 초기화하지 않고 진행
        advertisementDto.setDailyProfit(profit);
//        advertisementDto.setCalculatedDate(LocalDate.now());
//        Thread.sleep(10);
        advertisementDto.setCalculatedDate(LocalDate.parse(batchDate));
        long videoNumber = advertisementDto.getVideoNumber();
//        advertisementsProfitCache.addAdProfitInVideo(videoNumber, profit);
        cacheService.putToCache(videoNumber, profit);
         return advertisementDto;
    }
}
