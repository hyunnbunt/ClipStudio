package clipstudio.dto.profit;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class DailyProfitOfVideoDto {
    long videoNumber;
    long videoProfit;
    long advertisementProfit;
}
