package clipstudio.dto.domain;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class AdvertisementDto {
    public Long number;
    public VideoDto video;
    public int orderInVideo;
    public Long totalViews;
    public long tempDailyViews; // 최근 하루 동안의 조회수 => 정산시 비우고 AdvertisementDailyProfitDto 에 합침
    public double dailyProfit;
    public long dailyViews;
    public LocalDate calculatedDate;
}
