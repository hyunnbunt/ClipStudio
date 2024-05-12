package clipstudio.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
@Builder
public class DailyProfitOfVideo {
    public Long videoNumber; // video number 와 일치
    public Double dailyProfit;
    public Date calculatedDate;
    public static Long[] views = new Long[]{0L, 100000L, 500000L, 1000000L, Long.MAX_VALUE};
    public static Float[] won = new Float[]{1f, 1.1f, 1.3f, 1.5f};
    public static final List<Map<String, Object>> priceTable = PriceTable.getPriceTable(views, won);
}
