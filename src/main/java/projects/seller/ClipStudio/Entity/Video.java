package projects.seller.ClipStudio.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import projects.seller.ClipStudio.VideoDto;
import projects.seller.ClipStudio.oauth2.User.entity.User;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long number;
    @Column
    public String title;
    @Column
    public int duration; // seconds, java.sql.Time <-> java.sql.jdbcType 비교할 것. db에는 time 이 있다 (mm:ssssss)
    @ManyToOne
    @JoinColumn(nullable = false)
    public User uploader;
    @Column
    public Timestamp createdDate;
    @Column
    public Long views;

    public static Video fromDto(VideoDto videoDto) {
        return builder()
                .title(videoDto.title)
                .views(videoDto.views).build();
    }

//    public void updatePriceIdx(List<Integer> division) {
//        for (int i = 0; i < division.size()-1; i ++) {
//            int start = division.get(i);
//            int end = division.get(i+1);
//            if (this.views>=start&&this.views<end) {
//                priceSectionIdx = i;
//                // 0, 10, 50, 100 => 25 => 1 (두 번째 구간)
//            }
//        }
//    }
    public void increaseViews() {
        this.views ++;
    }
}
