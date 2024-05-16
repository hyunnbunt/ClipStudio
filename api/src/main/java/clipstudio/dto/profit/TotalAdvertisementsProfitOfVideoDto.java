package clipstudio.dto.profit;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class TotalAdvertisementsProfitOfVideoDto {
    long videoNumber;
    double totalProfitOfAllAdvertisementsInVideo;
    LocalDate calculatedDate;
}
