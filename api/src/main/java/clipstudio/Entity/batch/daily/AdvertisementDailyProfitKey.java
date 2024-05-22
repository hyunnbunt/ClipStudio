package clipstudio.Entity.batch.daily;

import jakarta.persistence.Column;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AdvertisementDailyProfitKey implements Serializable {
    @Column(name = "video_number", nullable = false)
    long advertisementNumber;
    @Column(name = "calculated_date", nullable = false)
    LocalDate calculatedDate;
}
