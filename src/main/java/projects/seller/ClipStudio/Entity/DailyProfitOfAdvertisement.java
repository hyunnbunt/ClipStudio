package projects.seller.ClipStudio.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class DailyProfitOfAdvertisement {
    @Id
    Long AdvertisementNumber; // 광고 넘버와 일치
    @Column(nullable = false)
    Long dailyProfit;
    @Column(nullable = false)
    Date date;
}