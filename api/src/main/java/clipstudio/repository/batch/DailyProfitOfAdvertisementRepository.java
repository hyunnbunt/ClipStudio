package clipstudio.repository.batch;

import clipstudio.entity.profit.AdvertisementProfit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DailyProfitOfAdvertisementRepository extends JpaRepository<AdvertisementProfit, Long> {
    public AdvertisementProfit findByAdvertisementNumberAndCalculatedDate(Long advertisementNumber, LocalDate calculatedDate);
}
