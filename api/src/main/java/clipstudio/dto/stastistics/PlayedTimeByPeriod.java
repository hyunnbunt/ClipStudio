package clipstudio.dto.stastistics;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlayedTimeByPeriod {
    Long videoNumber;
    Long playedTimeByPeriod; // seconds
}
