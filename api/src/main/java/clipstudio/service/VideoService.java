package clipstudio.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import clipstudio.Entity.Video;
import clipstudio.dto.VideoDto;
import clipstudio.repository.VideoRepository;

@Service
@Slf4j
public class VideoService {
    private final VideoRepository videoRepository;
    public VideoService(VideoRepository videoRepository) {
        // constructor가 하나일 때는 @Autowired 생략 가능
        this.videoRepository = videoRepository;
    }
    public VideoDto increaseViews(@PathVariable Long videoNumber) {
        Video target = videoRepository.getReferenceById(videoNumber); //getId() is deprecated
        target.setTotalViews(target.getTotalViews()+1);
        Video updated = videoRepository.save(target);
        return VideoDto.fromEntity(updated);
    }

    public VideoDto testNewVideo(@RequestBody VideoDto videoDto) {
        log.info("service, post mapping to /videos/test/new" + videoDto.toString());
        Video newVideo = Video.fromDto(videoDto);
        Video created = videoRepository.save(newVideo);
        return VideoDto.fromEntity(created);
    }
}
