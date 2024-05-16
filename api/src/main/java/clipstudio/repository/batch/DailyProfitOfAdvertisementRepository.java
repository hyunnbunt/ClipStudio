package clipstudio.repository.batch;

import clipstudio.Entity.batch.daily.AdvertisementDailyProfit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DailyProfitOfAdvertisementRepository extends JpaRepository<AdvertisementDailyProfit, Long> {
    public AdvertisementDailyProfit findByAdvertisementNumberAndCalculatedDate(Long advertisementNumber, LocalDate calculatedDate);
}
