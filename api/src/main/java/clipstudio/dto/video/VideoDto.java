package clipstudio.dto.video;

import lombok.*;
import clipstudio.entity.Video;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data // A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, @Setter on all non-final fields, and @RequiredArgsConstructor
public class VideoDto {
    public Long id;
    public int durationSec;
    public Long uploaderNumber;
    public LocalDate createdDate;
    public String title;
    public Long totalViews;
    public Long tempDailyViews;
    public static VideoDto fromEntity(Video videoEntity) {
        return builder()
                .id(videoEntity.getNumber())
                .durationSec(videoEntity.getDurationSec())
                .uploaderNumber(videoEntity.getUploader().getNumber())
                .createdDate(videoEntity.getCreatedDate())
                .title(videoEntity.getTitle())
                .totalViews(videoEntity.getTotalViews())
                .tempDailyViews(videoEntity.getTodayViews())
                .build();
    }
}
