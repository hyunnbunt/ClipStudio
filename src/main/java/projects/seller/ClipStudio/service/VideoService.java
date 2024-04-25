package projects.seller.ClipStudio.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import projects.seller.ClipStudio.Entity.Video;
import projects.seller.ClipStudio.VideoDto;
import projects.seller.ClipStudio.repository.VideoRepository;

@Service
@Slf4j
public class VideoService {
    private final VideoRepository videoRepository;
    public VideoService(VideoRepository videoRepository) {
        // di 객체가 하나일 때는 @Autowired 생략 가능
        this.videoRepository = videoRepository;
    }
    public VideoDto increaseViews(@PathVariable Long videoId) {
        Video target = videoRepository.getReferenceById(videoId); //getId() is deprecated
        target.increaseViews();
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
