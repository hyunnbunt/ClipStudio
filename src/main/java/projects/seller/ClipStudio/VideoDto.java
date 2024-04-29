package projects.seller.ClipStudio;

import lombok.*;
import projects.seller.ClipStudio.Entity.Video;

import java.sql.Timestamp;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data // A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, @Setter on all non-final fields, and @RequiredArgsConstructor
public class VideoDto {
    public Long id;
    public int duration;
    public Timestamp createdDate;
    public String title;
    public Long views;
    public Integer priceIdx;
    public static VideoDto fromEntity(Video videoEntity) {
        return builder()
                .id(videoEntity.number)
                .duration(videoEntity.duration)
                .createdDate(videoEntity.createdDate)
                .title(videoEntity.title)
                .views(videoEntity.views)
                .build();
    }
}
