package clipstudio.dto.profit;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DailyProfitDto {
    LocalDate date;
    List<ProfitDto> dailyProfit;
}
