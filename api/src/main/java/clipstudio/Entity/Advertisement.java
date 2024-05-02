package projects.seller.ClipStudio.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name="advertisements")
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long number;
    @ManyToOne
    @JoinColumn(nullable = false, name="video_number")
    public Video video;
    @Column(nullable = false)
    public Integer orderInVideo; // order in video.
    @Column(nullable = false)
    public Long totalViews;
    @Column(nullable = false)
    public Long dailyViews; // 최근 하루 동안의 조회수 => 정산시 비우고 views 에 합침
    @Column
    public Long profit; // 수익 신청 안 했으면 null 가능
}
