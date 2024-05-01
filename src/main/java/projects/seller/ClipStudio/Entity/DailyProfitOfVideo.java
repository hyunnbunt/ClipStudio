package projects.seller.ClipStudio.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class DailyProfitOfVideo {
    @Id
    Long videoNumber; // video number 와 일치
    @Column(nullable = false)
    Long dailyProfit;
    @Column(nullable = false)
    Date calculatedDate;
}
