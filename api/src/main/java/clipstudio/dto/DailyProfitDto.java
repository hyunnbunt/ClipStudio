package clipstudio.dto;

import lombok.*;

import java.time.LocalDate;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DailyProfitDto {
    long videoNumber;
    double videoProfit;
    double totalProfitOfAllAdvertisementsInVideo;
    LocalDate calculatedDate;
    public static void updateDailyProfitOfVideo() {

    }
}
