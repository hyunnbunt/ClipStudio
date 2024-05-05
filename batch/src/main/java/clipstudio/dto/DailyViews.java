package clipstudio.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Builder
@Getter
public class DailyViews {
    public String title;
    public Long number;
    public Timestamp createdDate;
    public Integer duration;
    public Long dailyViews;
    public Long totalViews;
}
