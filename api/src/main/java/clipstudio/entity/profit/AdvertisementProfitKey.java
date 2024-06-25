package clipstudio.entity.profit;

import jakarta.persistence.Column;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AdvertisementProfitKey implements Serializable {
    @Column(name = "advertisement_number", nullable = false)
    long advertisementNumber;
    @Column(name = "date", nullable = false)
    LocalDate date;
}
