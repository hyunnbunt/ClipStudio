package clipstudio.Entity.batch.daily;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class VideoDailyProfitKey implements Serializable {
    @Column(name = "video_number", nullable = false)
    long videoNumber;
    @Column(name = "calculated_date", nullable = false)
    LocalDate calculatedDate;
}
