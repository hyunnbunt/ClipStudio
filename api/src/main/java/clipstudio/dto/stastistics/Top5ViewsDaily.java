package clipstudio.dto.stastistics;

import clipstudio.Entity.batch.daily.VideoDailyProfit;
import clipstudio.Entity.batch.daily.VideoDailyProfitKey;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Top5ViewsDaily {
    LocalDate date;
    List<VideoDailyProfit> top5;
    public Top5ViewsDaily(LocalDate date) {
        this.date = date;
        top5 = new ArrayList<>();
    }

    public void addTop5(VideoDailyProfit videoDailyProfit) {
        this.top5.add(videoDailyProfit);
    }
}
