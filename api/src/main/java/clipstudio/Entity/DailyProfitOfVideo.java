package clipstudio.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@Entity
public class DailyProfitOfVideo {
    @Id
    Long videoNumber; // video number 와 일치
    @Column(nullable = false)
    Double dailyProfit;
    @Column(nullable = false)
    Date calculatedDate;
}
