package clipstudio.dto.stastistics;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ViewsByPeriod {
    Long videoNumber;
    Long viewsByPeriod;
}
