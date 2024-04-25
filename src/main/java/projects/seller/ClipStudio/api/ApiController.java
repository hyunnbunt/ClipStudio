package projects.seller.ClipStudio.api;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projects.seller.ClipStudio.Entity.Video;
import projects.seller.ClipStudio.VideoDto;
import projects.seller.ClipStudio.service.VideoService;

import java.net.http.HttpResponse;

@RestController
@Slf4j
@RequestMapping("/")
public class ApiController {
    private final VideoService videoService;
    public ApiController(VideoService videoService) {
        this.videoService = videoService;
    }
    @PutMapping("api/videos/{video_id}")
    public ResponseEntity<VideoDto> increaseViews(@PathVariable Long videoId) {
        log.info(videoId.toString());
        // video 조회, 영상 조회수 증가
        try {
            return ResponseEntity.status(HttpStatus.OK).body(videoService.increaseViews(videoId));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("api/videos/test/new")
    public VideoDto testNewVideo(@RequestBody VideoDto videoDto) {
        log.info("controller, post mapping to /videos/test/new" + videoDto.toString());
        return videoService.testNewVideo(videoDto);
    }
}
