package clipstudio.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.sql.Date;
import java.time.LocalDate;

@Builder
@Getter
public class Video {
    public String title;
    public Long number;
    public LocalDate createdDate;
    public Integer durationSec;
    public Long tempDailyViews;
    public Long totalViews;
}
