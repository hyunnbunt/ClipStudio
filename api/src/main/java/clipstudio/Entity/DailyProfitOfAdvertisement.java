package clipstudio.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Data
public class DailyProfitOfAdvertisement {
    @Id
    Long AdvertisementNumber; // 광고 넘버와 일치
    @Column(nullable = false)
    Long dailyProfit;
    @Column(nullable = false)
    Date calculatedDate;
}
