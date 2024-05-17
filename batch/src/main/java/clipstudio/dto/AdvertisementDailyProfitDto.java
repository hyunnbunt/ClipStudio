package clipstudio.dto;

import clipstudio.util.ProfitCalculator;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
@Setter
@Getter
@Builder
public class AdvertisementDailyProfitDto {
        public Long advertisementNumber; // advertisement number 와 일치
        public long dailyViews;
        public double dailyProfit;
        public double totalProfit;
        public LocalDate calculatedDate;
        private static Long[] views = new Long[]{0L, 100000L, 500000L, 1000000L, Long.MAX_VALUE};
        private static Float[] won = new Float[]{10f, 12f, 15f, 20f};
        public static final List<Map<String, Object>> priceTable = ProfitCalculator.getPriceTable(views, won);
}
