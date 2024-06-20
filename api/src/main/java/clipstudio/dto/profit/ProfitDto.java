package clipstudio.dto.profit;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfitDto {
    LocalDate date;
    List<ProfitDetail> dailyProfit;
    public ProfitDto(LocalDate date) {
        this.date = date;
        this.dailyProfit = new ArrayList<>();
    }

    public ProfitDto addProfit(ProfitDetail profitDetail) {
        this.getDailyProfit().add(profitDetail);
        return this;
    }
}
