package clipstudio.repository;

import clipstudio.Entity.daily.DailyProfitOfVideo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyProfitOfVideoRepository extends JpaRepository<DailyProfitOfVideo, Long> {
    Optional<DailyProfitOfVideo> findByVideoNumberAndCalculatedDate(Long videoNumber, LocalDate calculatedDate);
}
