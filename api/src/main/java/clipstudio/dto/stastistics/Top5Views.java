package clipstudio.dto.stastistics;

import clipstudio.entity.profit.TotalProfit;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Top5Views {
    LocalDate date;
    List<TotalProfit> top5;
    public Top5Views(LocalDate date) {
        this.date = date;
        top5 = new ArrayList<>();
    }
    public void addToTop5(TotalProfit totalProfit) {
        this.top5.add(totalProfit);
    }
}
