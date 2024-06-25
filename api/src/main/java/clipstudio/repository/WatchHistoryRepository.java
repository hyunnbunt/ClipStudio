package clipstudio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import clipstudio.entity.WatchHistory;

import java.util.Optional;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {

    Optional<WatchHistory> findByUserEmailAndVideoNumber(String userEmail, long videoNumber);
}
