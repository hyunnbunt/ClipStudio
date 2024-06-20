package clipstudio.dto.stastistics;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Top5PlayedTimeByPeriod {
    LocalDate startDate;
    LocalDate endDate;
    List<PlayedTimeByPeriod> playedTimeByPeriods;
    public Top5PlayedTimeByPeriod(LocalDate start, LocalDate end) {
        this.startDate = start;
        this.endDate = end;
        this.playedTimeByPeriods = new ArrayList<>();
    }
}
