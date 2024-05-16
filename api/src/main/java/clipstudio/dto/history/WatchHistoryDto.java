package clipstudio.dto.history;

import lombok.*;
import clipstudio.Entity.WatchHistory;

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
                .videoStoppedTime(watchHistory.getVideoStoppedTimeSec())
                .build();
    }
}
