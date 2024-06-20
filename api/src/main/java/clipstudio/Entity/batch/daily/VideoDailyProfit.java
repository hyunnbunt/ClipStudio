package clipstudio.Entity.batch.daily;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name="video_daily_profit")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(VideoDailyProfitKey.class)
public class VideoDailyProfit  {
    @Id
    @Column(name = "video_number", nullable = false)
    long videoNumber;
    @Id
    @Column(name = "calculated_date", nullable = false)
    LocalDate calculatedDate;
    @Column(nullable = false)
    Long uploaderNumber;
    @Column(nullable = false)
    long dailyViews;
    @Column(nullable = false)
    long dailyPlayedSec;
    @Column(nullable = false)
    double dailyProfitOfVideo;
    double dailyTotalProfitOfAdvertisements;
}
