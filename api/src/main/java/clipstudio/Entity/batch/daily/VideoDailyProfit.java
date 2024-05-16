package clipstudio.Entity.batch.daily;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name="video_daily_histories")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoDailyProfit {
    @Id
    @PrimaryKeyJoinColumn // 공부할 것
    long videoNumber; // video number 와 일치
    @Column(nullable = false)
    long dailyViews;
    @Column(nullable = false)
    double dailyProfit;
    @Column(nullable = false)
    LocalDate calculatedDate;
}
