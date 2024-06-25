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
    public LocalDate date;
    public double profit;
    public int orderInVideo;
    public long todayViews; // 최근 하루 동안의 조회수 => 정산시 비우고 AdvertisementProfitDto 에 합침
    public long totalViews; // 수익 구간 확인 위해 필요
    private static long[] views = new long[]{0L, 100000L, 500000L, 1000000L, Long.MAX_VALUE}; // 수익 변화 구간
    private static float[] won = new float[]{10f, 12f, 15f, 20f}; // 구간별 수익
    public static final List<Map<String, Object>> priceTable = ProfitCalculator.getPriceTable(views, won);
}
