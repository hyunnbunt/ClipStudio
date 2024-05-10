package clipstudio.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Data
@Table(name="advertisement_daily_profits")
public class DailyProfitOfAdvertisement {
    @Id
    Long AdvertisementNumber; // 광고 넘버와 일치
    @Column(nullable = false)
    Long dailyProfit;
    @Column(nullable = false)
    Date calculatedDate;
}
