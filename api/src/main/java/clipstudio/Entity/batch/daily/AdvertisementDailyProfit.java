package clipstudio.Entity.batch.daily;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name="advertisement_daily_profit")
@IdClass(AdvertisementDailyProfitKey.class)
public class AdvertisementDailyProfit {
    @Id
    @Column(nullable = false)
    long advertisementNumber;
    @Id
    @Column(nullable = false)
    LocalDate calculatedDate;
    @Column(nullable = false)
    double dailyProfit;
    @Column(nullable = false)
    long dailyViews;
}
