package clipstudio.repository.batch;

import clipstudio.Entity.batch.daily.VideoDailyProfit;
import clipstudio.Entity.batch.daily.VideoDailyProfitKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProfitRepository extends JpaRepository<VideoDailyProfit, VideoDailyProfitKey> {
    Optional<VideoDailyProfit> findById(VideoDailyProfitKey videoDailyProfitKey);

    @Query(value = "SELECT * FROM video_daily_profit WHERE uploader_number = :uploaderNumber AND calculated_date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<VideoDailyProfit> findAllByUploaderNumberAndDateBetween(@Param("uploaderNumber") Long uploaderNumber, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
