package projects.seller.ClipStudio.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.ManyToAny;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Video {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column
    Time duration; // seconds, java.sql.Time <-> java.sql.jdbcType 비교할 것. db에는 time 이 있다 (mm:ssssss)
    @Column
    Timestamp createdDate;
    @Column
    String title;
    @Column
    Long views;
    @Column
    Integer priceIdx;
    @Column
    List<Long> adViewsPerPrice;
    public void updatePriceIdx(List<Integer> division) {
        for (int i = 0; i < division.size()-1; i ++) {
            int start = division.get(i);
            int end = division.get(i+1);
            if (this.views>=start&&this.views<end) {
                priceIdx = i;
                // 0, 10, 50, 100 => 25 => 1 (두 번째 구간)
            }
        }
    }
}
