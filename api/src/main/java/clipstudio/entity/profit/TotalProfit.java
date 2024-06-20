package clipstudio.entity.profit;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="total_profit")
@IdClass(TotalProfitKey.class)
public class TotalProfit {
    @Id
    @Column(name = "video_number", nullable = false)
    long videoNumber;
    @Id
    @Column(name = "date", nullable = false)
    LocalDate date;
    @Column(nullable = false)
    Long uploaderNumber;
    @Column(nullable = false)
    long dailyViews;
    @Column(nullable = false)
    double videoProfit;
    @Column(nullable = false)
    long dailyPlayedSec;
    @Column
    double advertisementsProfit;
}
