package projects.seller.ClipStudio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projects.seller.ClipStudio.Entity.WatchHistory;

import java.util.Optional;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {

    Optional<WatchHistory> findByUserEmailAndVideoNumber(String userEmail, long videoNumber);
}
