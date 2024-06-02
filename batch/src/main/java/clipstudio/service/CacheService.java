package clipstudio.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final RedisTemplate<Long, Double> redisTemplate;

    public void putToCache(Long key, Double value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Double getFromCache(Long key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void addAdvertisementProfitToCache(long videoNumber, double profit) {
        Double prevProfit = this.getFromCache(videoNumber);
        if (prevProfit == null) {
            this.putToCache(videoNumber, profit);
            return;
        }
        this.putToCache(videoNumber, this.getFromCache(videoNumber) + profit);
    }
}

