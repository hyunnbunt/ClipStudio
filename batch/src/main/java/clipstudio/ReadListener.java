package clipstudio;

import clipstudio.dto.VideoDto;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.StepExecution;

public class ReadListener implements ItemReadListener<VideoDto> {
    @Override
    public void afterRead(VideoDto videoDto) {
        System.out.println("Reading item number = " + videoDto.getNumber());
    }
}
