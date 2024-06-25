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
    public LocalDate date;
    public long number;
    public Long uploaderNumber;
    public double videoProfit;
    public double advertisementsProfit;
    public String title;
    public int durationSec;
    public long todayViews;
    public long totalViews;
    public long todayPlayedSec;
    public static long[] views = new long[]{0L, 100000L, 500000L, 1000000L, Long.MAX_VALUE};
    public static float[] won = new float[]{1f, 1.1f, 1.3f, 1.5f};
    public static final List<Map<String, Object>> priceTable = ProfitCalculator.getPriceTable(views, won);
}
