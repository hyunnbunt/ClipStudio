package clipstudio.Entity;

import jakarta.persistence.*;

@Entity
public class VideoViewPrice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer number;
    @Column
    Integer startRangeOfViews; // start included
    @Column
    Integer endRangeOfViews; // end not included
    @Column
    Float price; // 원 단위
}
