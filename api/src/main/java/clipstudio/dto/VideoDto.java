package clipstudio.dto;

import lombok.*;
import clipstudio.Entity.Video;

import java.sql.Date;
import java.sql.Timestamp;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data // A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, @Setter on all non-final fields, and @RequiredArgsConstructor
public class VideoDto {
    public Long id;
    public int durationSec;
    public Date createdDate;
    public String title;
    public Long totalViews;
    public Long tempDailyViews;
    public Integer priceIdx;
    public static VideoDto fromEntity(Video videoEntity) {
        return builder()
                .id(videoEntity.getNumber())
                .durationSec(videoEntity.getDurationSec())
                .createdDate(videoEntity.getCreatedDate())
                .title(videoEntity.getTitle())
                .totalViews(videoEntity.getTotalViews())
                .tempDailyViews(videoEntity.getTempDailyViews())
                .build();
    }
}
