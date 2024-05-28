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
public class Top5ViewsByPeriod {
    LocalDate startDate;
    LocalDate endDate;
    List<ViewsByPeriod> viewsByPeriodList = new ArrayList<>();
}
