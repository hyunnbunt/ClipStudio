package clipstudio.dto.stastistics;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Top5ViewsByPeriod {
    LocalDate startDate;
    LocalDate endDate;
    List<ViewsByPeriod> videos;
    public Top5ViewsByPeriod(LocalDate start, LocalDate end) {
        this.startDate = start;
        this.endDate = end;
        this.videos = new ArrayList<>();
    }

    public void update(Long videoNumber, Long views) {
        this.videos.add(new ViewsByPeriod(videoNumber, views));
    }
}
