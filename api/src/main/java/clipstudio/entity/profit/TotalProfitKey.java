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
public class TotalProfitKey implements Serializable {
    @Column(name = "video_number", nullable = false)
    long videoNumber;
    @Column(name = "date", nullable = false)
    LocalDate date;
}
