package clipstudio.repository;

import clipstudio.Entity.daily.AdvertisementDailyHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementDailyHistoryRepository extends JpaRepository<AdvertisementDailyHistory, Long> {
}
