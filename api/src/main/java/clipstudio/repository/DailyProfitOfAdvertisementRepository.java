package clipstudio.repository;

import clipstudio.Entity.daily.DailyProfitOfAdvertisement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DailyProfitOfAdvertisementRepository extends JpaRepository<clipstudio.Entity.daily.DailyProfitOfAdvertisement, Long> {
    public DailyProfitOfAdvertisement findByAdvertisementNumberAndCalculatedDate(Long advertisementNumber, LocalDate calculatedDate);
}
