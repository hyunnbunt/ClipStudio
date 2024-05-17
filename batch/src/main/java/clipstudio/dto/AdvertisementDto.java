package clipstudio.dto;
import clipstudio.util.ProfitCalculator;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
public class AdvertisementDto {
    public Long number;
    public Long videoNumber;
    public int orderInVideo;
    public Long totalViews;
    public long tempDailyViews; // 최근 하루 동안의 조회수 => 정산시 비우고 AdvertisementDailyProfitDto 에 합침
    public double dailyProfit;
    public long dailyViews;
    public LocalDate calculatedDate;
    private static Long[] views = new Long[]{0L, 100000L, 500000L, 1000000L, Long.MAX_VALUE};
    private static Float[] won = new Float[]{10f, 12f, 15f, 20f};
    public static final List<Map<String, Object>> priceTable = ProfitCalculator.getPriceTable(views, won);
}
