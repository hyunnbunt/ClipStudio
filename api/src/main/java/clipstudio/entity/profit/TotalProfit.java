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
//@Table(name="total_profit")
//@Table(name="total_profit", indexes = {
//        @Index(name = "idx_total_profit", columnList = "date")
//})
@IdClass(TotalProfitKey.class)
public class TotalProfit {
    @Id
    @Column(name = "date", nullable = false)
    LocalDate date;
    @Id
    @Column(nullable = false)
    Long uploaderNumber;
    @Column(name = "video_number", nullable = false)
    long videoNumber;
    @Column(nullable = false)
    long views;
    @Column(nullable = false)
    double videoProfit;
    @Column(nullable = false)
    long playedSec;
    @Column
    double advertisementsProfit;
}
