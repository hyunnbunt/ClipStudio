package clipstudio.repository.batch;

import clipstudio.entity.profit.AdvertisementProfit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface AdvertisementProfitRepository extends JpaRepository<AdvertisementProfit, Long> {
    public AdvertisementProfit findByAdvertisementNumberAndDate(Long advertisementNumber, LocalDate date);
}
