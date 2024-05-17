package clipstudio.singleton;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class AdvertisementsProfitCache {
    public static HashMap<Long, Double> adProfitInVideo = new HashMap<>();
    public void addAdProfitInVideo(Long videoNumber, Double profit) {
        adProfitInVideo.put(videoNumber, adProfitInVideo.getOrDefault(videoNumber, 0d) + profit);
    }
    public double getAdProfitInVideo(Long videoNumber) {
        return adProfitInVideo.getOrDefault(videoNumber, 0d);
    }
}
