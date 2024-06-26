package clipstudio.entity.profit;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TotalProfitKey implements Serializable {
    @Column(name = "date", nullable = false)
    LocalDate date;
    @Column(name = "video_number", nullable = false)
    Long videoNumber;
}
