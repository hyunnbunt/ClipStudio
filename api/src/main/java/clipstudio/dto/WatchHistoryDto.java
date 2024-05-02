package projects.seller.ClipStudio.dto;

import lombok.*;
import projects.seller.ClipStudio.Entity.WatchHistory;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class WatchHistoryDto {
    String userEmail;
    Long videoNumber;
    Integer videoStoppedTime;
    public static WatchHistoryDto fromEntity(WatchHistory watchHistory) {
        return builder()
                .userEmail(watchHistory.getUser().getEmail())
                .videoNumber(watchHistory.getVideo().getNumber())
                .videoStoppedTime(watchHistory.getVideoStoppedTime())
                .build();
    }
}
