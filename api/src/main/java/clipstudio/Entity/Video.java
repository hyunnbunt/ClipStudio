package clipstudio.Entity;

import jakarta.persistence.*;
import lombok.*;
import clipstudio.dto.VideoDto;
import clipstudio.oauth2.User.entity.User;

import java.sql.Timestamp;

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
    public Integer duration; // seconds, java.sql.Time <-> java.sql.jdbcType 비교할 것. db에는 time 이 있다 (mm:ssssss)
    @ManyToOne
    @JoinColumn(nullable = false, name = "uploader_number")
    public User uploader;
    @Column(nullable = false)
    public Timestamp createdDate;
    @Column(nullable = false)
    public Long totalViews;
    @Column(nullable = false)
    public Long dailyViews;
    public static Video fromDto(VideoDto videoDto) {
        return builder()
                .title(videoDto.title)
                .totalViews(videoDto.views).build();
    }
    public void increaseViews() {
        this.totalViews++;
    }
}
