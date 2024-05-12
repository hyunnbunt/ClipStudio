package clipstudio.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.sql.Date;

@Builder
@Getter
public class Video {
    public String title;
    public Long number;
    public Date createdDate;
    public Integer durationSec;
    public Long tempDailyViews;
    public Long totalViews;
}
