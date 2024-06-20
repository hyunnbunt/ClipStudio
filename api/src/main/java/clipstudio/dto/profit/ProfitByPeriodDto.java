package clipstudio.dto.profit;

import clipstudio.Entity.batch.daily.VideoDailyProfit;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class ProfitByPeriodDto {
    LocalDate startDate;
    LocalDate endDate;
    Map<LocalDate, DailyProfitDto> profit;
    public ProfitByPeriodDto(LocalDate start, LocalDate end) {
        this.startDate = start;
        this.endDate = end;
        this.profit = new HashMap<>();
    }
    public void updateProfit(VideoDailyProfit videoDailyProfit) {
        LocalDate curr = videoDailyProfit.getCalculatedDate();
        this.profit.put(curr, profit.getOrDefault(curr, new DailyProfitDto(curr)).addProfit(ProfitDto.fromEntity(videoDailyProfit)));
    }
}
