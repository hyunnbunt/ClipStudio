package clipstudio.processor;

import clipstudio.cache.AdvertisementsProfitCache;
import clipstudio.util.ProfitCalculator;
import clipstudio.dto.VideoDto;
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
public class VideoProfitProcessor implements ItemProcessor<VideoDto, VideoDto> {
    final AdvertisementsProfitCache advertisementsProfitCache;
    @Value("#{jobParameters['batchDate']}")
    private String batchDate;
    @Override
    public VideoDto process(VideoDto videoDto) throws Exception {
//        log.info("Inside advertisement step: " + Thread.currentThread());
//        log.info("Is thread virtual: " + Thread.currentThread().isVirtual());
        final Long prevTotal = videoDto.getTotalViews();
        final Long todayViews = videoDto.getTodayViews();
        double profit = ProfitCalculator.getDailyProfit(prevTotal, todayViews, "video");
        videoDto.setTotalViews(prevTotal + todayViews);
        videoDto.setTodayViews(todayViews); // 테스트시 동일 데이터 2번 돌리기 위해 초기화하지 않고 진행
        videoDto.setVideoProfit(profit);
//        Thread.sleep(500);
        videoDto.setDate(LocalDate.parse(batchDate));
        videoDto.setAdvertisementsProfit(advertisementsProfitCache
                .getAdvertisementsProfit(videoDto.getNumber()));
        return videoDto;
    }
}