package clipstudio.dto.profit;

import clipstudio.Entity.batch.daily.VideoDailyProfit;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DailyProfitDto {
    LocalDate date;
    List<ProfitDto> dailyProfit;
    public DailyProfitDto(LocalDate date) {
        this.date = date;
        this.dailyProfit = new ArrayList<>();
    }

    public DailyProfitDto addProfit(ProfitDto profitDto) {
        this.getDailyProfit().add(profitDto);
        return this;
    }
}
