package clipstudio.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class VideoDailyHistory {
    long videoNumber; // video number 와 일치
    long updatedTotalViews;
    long dailyViews;
    double dailyProfit;
    Date calculatedDate;
}
