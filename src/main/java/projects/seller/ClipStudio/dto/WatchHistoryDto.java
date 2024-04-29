package projects.seller.ClipStudio.dto;

import lombok.Data;

@Data
public class WatchHistoryDto {
    long userNumber;
    long videoNumber;
    int videoStoppedTime;
}
