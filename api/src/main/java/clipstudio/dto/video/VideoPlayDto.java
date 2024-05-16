package clipstudio.dto.video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoPlayDto {
    Integer videoStoppedTime;
    ZonedDateTime requestTime;
}
