package clipstudio.dto.video;

import clipstudio.oauth2.User.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class VideoUploadDto {
    public String title;
    public int durationSec;
    public User uploader;
}
