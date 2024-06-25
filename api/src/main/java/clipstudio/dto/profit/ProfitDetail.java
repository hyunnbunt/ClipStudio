package clipstudio.dto.profit;

import clipstudio.entity.profit.TotalProfit;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfitDetail {
    long videoNumber;
    double videoProfit;
    double advertisementsProfit;
    double totalProfit;
    public static ProfitDetail fromEntity(TotalProfit totalProfit) {
        double videoProfit = totalProfit.getVideoProfit();
        double advertisementsProfit = totalProfit.getAdvertisementsProfit();
        return ProfitDetail.builder()
                .videoNumber(totalProfit.getVideoNumber())
                .videoProfit(videoProfit)
                .advertisementsProfit(advertisementsProfit)
                .totalProfit(videoProfit + advertisementsProfit).build();
    }
}
