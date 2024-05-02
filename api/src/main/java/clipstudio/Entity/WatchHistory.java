package projects.seller.ClipStudio.Entity;

import jakarta.persistence.*;
import lombok.*;
import projects.seller.ClipStudio.dto.WatchHistoryDto;
import projects.seller.ClipStudio.oauth2.User.entity.User;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
public class WatchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long number;
    @ManyToOne
    @JoinColumn(nullable = false)
    Video video;
    @ManyToOne
    @JoinColumn(nullable = false)
    User user;
    @Column
    int videoStoppedTime; // 최근 시청 기록 -> 업데이트될 때마다 Video 엔티티의 광고 시청 조회수 리스트 업데이트 여부 확인 후 필요시 업데이트
    public WatchHistory(User user, Video video, int videoStoppedTime) {
        this.setUser(user);
        this.setVideo(video);
        this.setVideoStoppedTime(videoStoppedTime);
    }
}
