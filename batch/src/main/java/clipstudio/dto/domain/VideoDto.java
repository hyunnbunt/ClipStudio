package clipstudio.dto.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class VideoDto {
    public String title;
    public long number;
    public int durationSec;
    public long tempDailyViews;
    public long totalViews;
    public double dailyViews;
    public double dailyProfit;
    public LocalDate calculatedDate;
}
