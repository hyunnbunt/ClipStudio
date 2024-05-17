package clipstudio.dto;

import clipstudio.util.ProfitCalculator;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    public static Long[] views = new Long[]{0L, 100000L, 500000L, 1000000L, Long.MAX_VALUE};
    public static Float[] won = new Float[]{1f, 1.1f, 1.3f, 1.5f};
    public static final List<Map<String, Object>> priceTable = ProfitCalculator.getPriceTable(views, won);
}
