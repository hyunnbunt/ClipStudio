package clipstudio.dto.profit;

import lombok.*;

import java.time.LocalDate;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DailyProfitDto {
    long videoNumber;
    LocalDate calculatedDate;
    double profitOfVideo;
    double profitOfAdvertisements;
    double profitTotal;
}
