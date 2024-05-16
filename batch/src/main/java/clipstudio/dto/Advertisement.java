package clipstudio.dto;
import lombok.*;
import java.sql.Date;
import java.time.LocalDate;

@Builder
@Getter
public class Advertisement {
    public Long number;
    public Video video;
    public int orderInVideo;
    public LocalDate createdDate;
    public Long totalViews;
    public Long tempDailyViews; // 최근 하루 동안의 조회수 => 정산시 비우고 views 에 합침
}
