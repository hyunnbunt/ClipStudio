package clipstudio.dto.profit;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyProfitDto {
    List<DailyProfitDto> weeklyProfit;
    LocalDate startDate;
    LocalDate endDate;
}
