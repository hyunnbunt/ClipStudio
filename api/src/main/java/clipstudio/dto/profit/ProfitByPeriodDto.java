package clipstudio.dto.profit;

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
    Map<LocalDate, DailyProfitDto> daily;
    public ProfitByPeriodDto(LocalDate start, LocalDate end) {
        this.startDate = start;
        this.endDate = end;
        this.daily = new HashMap<>();
    }
}
