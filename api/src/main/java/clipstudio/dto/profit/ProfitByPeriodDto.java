package clipstudio.dto.profit;

import clipstudio.entity.profit.TotalProfit;
import lombok.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class ProfitByPeriodDto {
    LocalDate startDate;
    LocalDate endDate;
    Map<LocalDate, ProfitDetail> profit;
    public ProfitByPeriodDto(LocalDate start, LocalDate end) {
        this.startDate = start;
        this.endDate = end;
        this.profit = new HashMap<>();
    }
    public void add(TotalProfit totalProfit) {
        LocalDate curr = totalProfit.getDate();
        this.profit.put(curr, profit.getOrDefault(curr, ProfitDetail.fromEntity(totalProfit)));
    }
}
