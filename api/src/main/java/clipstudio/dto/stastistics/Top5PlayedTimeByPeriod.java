package clipstudio.dto.stastistics;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Top5PlayedTimeByPeriod {
    LocalDate startDate;
    LocalDate endDate;
    List<PlayedTimeByPeriod> playedTimeByPeriods = new ArrayList<>();
}
