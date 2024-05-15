package clipstudio.repository;

import clipstudio.Entity.Advertisement;
import clipstudio.Entity.VideoDailyHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface VideoDailyHistoryRepository extends JpaRepository<clipstudio.Entity.VideoDailyHistory, Long> {
    Optional<VideoDailyHistory> findByVideoNumberAndCalculatedDate(Long videoNumber, Date calculatedDate);
}
