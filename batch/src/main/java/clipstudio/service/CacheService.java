package clipstudio.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
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
        log.info(prevProfit + "");
        if (prevProfit == null) {
            prevProfit = 0d;
        }
        this.putToCache(videoNumber, prevProfit + profit);
    }
}

