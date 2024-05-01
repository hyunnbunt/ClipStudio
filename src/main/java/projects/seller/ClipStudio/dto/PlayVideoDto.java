package projects.seller.ClipStudio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayVideoDto {
    Integer videoStoppedTime;
    ZonedDateTime requestTime;
}
