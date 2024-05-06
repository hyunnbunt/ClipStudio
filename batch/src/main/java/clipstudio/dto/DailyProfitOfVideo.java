package clipstudio.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Builder
public class DailyProfitOfVideo {

    Long videoNumber; // video number 와 일치

    Long dailyProfit;

    Date calculatedDate;
}
