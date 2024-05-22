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
    LocalDate startDate;
    LocalDate endDate;
    List<DailyProfitDto> weeklyProfit;
}
