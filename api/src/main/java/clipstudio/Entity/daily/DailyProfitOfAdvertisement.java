package clipstudio.Entity.daily;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name="advertisement_daily_histories")
public class DailyProfitOfAdvertisement {
    @Id
    @PrimaryKeyJoinColumn // 공부할 것
    long advertisementNumber;
    @Column(nullable = false)
    double dailyProfit;
    @Column(nullable = false)
    long dailyViews;
    @Column(nullable = false)
    LocalDate calculatedDate;
}