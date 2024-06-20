package clipstudio.dto.profit;

import clipstudio.Entity.batch.daily.VideoDailyProfit;
import lombok.*;

import java.time.LocalDate;
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfitDto {
    long videoNumber;
    double profitOfVideo;
    double profitOfAdvertisements;
    double profitTotal;
    public static ProfitDto fromEntity(VideoDailyProfit videoDailyProfit) {
        double videoProfit =  videoDailyProfit.getDailyProfitOfVideo();
        double advertisementsProfit = videoDailyProfit.getDailyTotalProfitOfAdvertisements();
        return ProfitDto.builder()
                .videoNumber(videoDailyProfit.getVideoNumber())
                .profitOfVideo(videoProfit)
                .profitOfAdvertisements(advertisementsProfit)
                .profitTotal(videoProfit + advertisementsProfit).build();
    }
}
