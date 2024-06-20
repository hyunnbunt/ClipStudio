package clipstudio.dto.video;

import clipstudio.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class VideoUploadDto {
    public String title;
    public int durationSec;
}
