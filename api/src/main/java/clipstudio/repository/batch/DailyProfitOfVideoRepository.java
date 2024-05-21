package clipstudio.repository.batch;

import clipstudio.Entity.batch.daily.VideoDailyProfit;
import clipstudio.Entity.batch.daily.VideoDailyProfitKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyProfitOfVideoRepository extends JpaRepository<VideoDailyProfit, VideoDailyProfitKey> {
    Optional<VideoDailyProfit> findById(VideoDailyProfitKey videoDailyProfitKey);
}
