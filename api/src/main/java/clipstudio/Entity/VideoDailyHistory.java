package clipstudio.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@Entity
@Table(name="video_daily_histories")
public class VideoDailyHistory {
    @Id
    @PrimaryKeyJoinColumn // 공부할 것
    long videoNumber; // video number 와 일치
    @Column(nullable = false)
    long dailyViews;
    @Column(nullable = false)
    double dailyProfit;
    @Column(nullable = false)
    Date calculatedDate;
}
