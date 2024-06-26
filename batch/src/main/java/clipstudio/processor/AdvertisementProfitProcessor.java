package clipstudio.processor;

import clipstudio.cache.AdvertisementsProfitCache;
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
//        log.info("Inside advertisementDto step: " + Thread.currentThread());
//        log.info("Advertisement number:" + advertisementDto.getNumber());
        advertisementDto.setDate(LocalDate.parse(batchDate));
        final Long prevTotalViews = advertisementDto.getTotalViews();
        final Long todayViews = advertisementDto.getTodayViews();
        double profit = ProfitCalculator.getDailyProfit(prevTotalViews, todayViews, "advertisement");
        advertisementDto.setTotalViews(prevTotalViews + todayViews);
        advertisementDto.setTodayViews(todayViews); // multi, single thread 환경에서 동일 데이터 2번 돌리기 위해 초기화하지 않고 진행
        advertisementDto.setProfit(profit);
//        advertisementDto.setCalculatedDate(LocalDate.now());
//        Thread.sleep(10);
        advertisementDto.setDate(LocalDate.parse(batchDate));
        long videoNumber = advertisementDto.getVideoNumber();
        advertisementsProfitCache.addAdProfitInVideo(videoNumber, advertisementDto.getNumber(), profit);
         return advertisementDto;
    }
}
