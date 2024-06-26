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
public class TotalProfit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long number;
    @Column(nullable = false)
    LocalDate date;
    @Column(nullable = false)
    long uploaderNumber;
    @Column(nullable = false)
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
