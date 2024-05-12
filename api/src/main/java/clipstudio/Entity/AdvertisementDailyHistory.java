package clipstudio.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name="advertisement_daily_histories")
public class AdvertisementDailyHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    long advertisementNumber;
    @Column(nullable = false)
    long dailyProfit;
    @Column(nullable = false)
    long dailyViews;
    @Column(nullable = false)
    Date calculatedDate;
}
