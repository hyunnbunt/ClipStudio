package clipstudio.service;

import clipstudio.dto.video.VideoUploadDto;
import clipstudio.oauth2.User.User;
import clipstudio.oauth2.User.userRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import clipstudio.Entity.Video;
import clipstudio.dto.video.VideoDto;
import clipstudio.repository.VideoRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
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

    public VideoDto uploadVideo(VideoUploadDto videoUploadDto, String userEmail) {
        User uploader = userRepository.findByEmail(userEmail).orElseThrow();
        videoUploadDto.setUploader(uploader);
        Video video = Video.fromDto(videoUploadDto);
        // 조회수 데이터 업데이트
        video.setTempDailyViews(550000L);
        Video created = videoRepository.save(video);
        return VideoDto.fromEntity(created);
    }
}
