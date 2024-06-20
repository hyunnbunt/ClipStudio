package clipstudio.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class AdvertisementsProfitCache {
    public static ConcurrentHashMap<Long, Double> adProfitInVideo = new ConcurrentHashMap<>();
    public synchronized void addAdProfitInVideo(Long videoNumber, Long advertisementNumber, Double profit) {
        Double prevProfit = adProfitInVideo.getOrDefault(videoNumber, 0d);
        if (videoNumber.equals(1L)) {
            log.info("Video number: " + videoNumber + ", Advertisement number: " + advertisementNumber + ", profit: " + profit);
            adProfitInVideo.put(videoNumber, prevProfit + profit);
            log.info("Result: " + adProfitInVideo.get(videoNumber));
            return;
        }
        adProfitInVideo.put(videoNumber, prevProfit + profit);
    }
    public double getAdvertisementsProfit(Long videoNumber) {
        return adProfitInVideo.getOrDefault(videoNumber, 0d);
    }
}
