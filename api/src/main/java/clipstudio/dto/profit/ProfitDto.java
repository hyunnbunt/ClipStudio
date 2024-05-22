package clipstudio.dto.profit;

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
}
