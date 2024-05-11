package clipstudio.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name="advertisement_daily_histories")
public class AdvertisementDailyHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long number;
    @JoinColumn(nullable = false, name = "video_number")
    @ManyToOne
    Video video;
    @Column(nullable = false)
    int orderInVideo;
    @Column(nullable = false)
    Long dailyProfit;
    @Column(nullable = false)
    Date calculatedDate;
}
