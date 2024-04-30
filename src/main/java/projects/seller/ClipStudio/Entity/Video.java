package projects.seller.ClipStudio.Entity;

import jakarta.persistence.*;
import lombok.*;
import projects.seller.ClipStudio.dto.VideoDto;
import projects.seller.ClipStudio.oauth2.User.entity.User;

import java.sql.Timestamp;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="Videos")
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
