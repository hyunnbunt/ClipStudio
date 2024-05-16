package clipstudio.repository;

import clipstudio.Entity.daily.VideoDailyHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface VideoDailyHistoryRepository extends JpaRepository<VideoDailyHistory, Long> {
    Optional<VideoDailyHistory> findByVideoNumberAndCalculatedDate(Long videoNumber, LocalDate calculatedDate);
}
