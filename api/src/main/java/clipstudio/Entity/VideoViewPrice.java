package clipstudio.Entity;

import jakarta.persistence.*;

@Entity
public class VideoViewPrice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer number;
    @Column
    Integer start; // start included
    @Column
    Integer end; // end not included
    @Column
    Integer price; // 원 단위
}
