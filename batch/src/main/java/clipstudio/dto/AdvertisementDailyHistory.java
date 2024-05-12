package clipstudio.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class AdvertisementDailyHistory {
    public long advertisementNumber; // advertisement number와 일치
    public long updatedTotalViews;
    public long dailyViews;
    public double dailyProfit;
    Date calculatedDate;
}
