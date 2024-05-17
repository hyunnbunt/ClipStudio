package clipstudio.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Builder
public class TotalDailyProfitDto {
    public Long videoNumber; // video number 와 일치
    public VideoDailyProfitDto videoDailyProfitDto;
    public AdvertisementDailyProfitDto advertisementDailyProfitDto;
}
