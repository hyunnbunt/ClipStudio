package clipstudio.Entity;

import clipstudio.dto.video.VideoUploadDto;
import jakarta.persistence.*;
import lombok.*;
import clipstudio.dto.video.VideoDto;
import clipstudio.oauth2.User.User;

import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long number;
    @Column(nullable = false)
    public String title;
    @Column(nullable = false)
    public Integer durationSec; // seconds, java.sql.Time <-> java.sql.jdbcType 비교할 것. db에는 time 이 있다 (mm:ssssss)
    @ManyToOne
    @JoinColumn(nullable = false, name = "uploader_number")
    public User uploader;
    @Column(nullable = false)
    public LocalDate createdDate;
    @Column(nullable = false)
    public Long totalViews;
    @Column(nullable = false)
    public Long todayViews;
    @Column(nullable = false)
    public Long todayPlayedSec;
    public static Video fromDto(VideoDto videoDto) {
        return builder()
                .title(videoDto.getTitle())
                .totalViews(videoDto.getTotalViews()).build();
    }
    public static Video fromDto(VideoUploadDto videoUploadDto) {
        return builder()
                .title(videoUploadDto.getTitle())
                .uploader(videoUploadDto.getUploader())
                .durationSec(videoUploadDto.getDurationSec())
                .createdDate(LocalDate.now())
                .todayViews(0L)
                .totalViews(0L)
                .build();
    }
    public void increaseViews() {
        this.totalViews++;
    }
    public void updatePlayedSec(int playedSec) {this.todayPlayedSec += playedSec;}
}
