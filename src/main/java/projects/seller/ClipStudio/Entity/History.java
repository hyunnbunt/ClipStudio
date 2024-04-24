package projects.seller.ClipStudio.Entity;

import jakarta.persistence.*;
import projects.seller.ClipStudio.oauth2.User.entity.User;

import java.sql.Time;

@Entity
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    Video video;
    @ManyToOne
    User user;
    @Column
    Time latestWatch; // 최근 시청 기록 -> 업데이트될 때마다 Video 엔티티의 광고 시청 조회수 리스트 업데이트 여부 확인 후 필요시 업데이트
}
