package projects.seller.ClipStudio.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import projects.seller.ClipStudio.dto.VideoDto;
import projects.seller.ClipStudio.dto.WatchHistoryDto;
import projects.seller.ClipStudio.oauth2.User.oauth2.CustomOAuth2User;
import projects.seller.ClipStudio.service.WatchHistoryService;
//import projects.seller.ClipStudio.service.WatchHistoryService;

@RestController
@Slf4j
@RequestMapping("/")
@RequiredArgsConstructor
public class ApiController {

    private final WatchHistoryService watchHistoryService;

    // 어떤 유저가 비디오를 재생했을 때, 이미 히스토리가 있으면 있는 걸 보내주고, 없으면 새로 만들어서 보내줄 것.
    @GetMapping("/api/videos/{videoNumber}")
    public WatchHistoryDto playVideo(@PathVariable long videoNumber) {
        log.info(String.valueOf(videoNumber));
        return new WatchHistoryDto();
    }

    @PostMapping("/api/videos/{videoNumber}")
    public ResponseEntity<WatchHistoryDto> updateWatchHistory(@PathVariable long videoNumber,
                                                              @RequestBody WatchHistoryDto watchHistoryDto,
                                                              @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        log.info("post mapping");
        log.info(String.valueOf(videoNumber));
        log.info(customOAuth2User.getEmail());
        log.info(String.valueOf(watchHistoryDto.toString()));
        WatchHistoryDto updatedWatchHistory = watchHistoryService.updateWatchHistory(videoNumber, watchHistoryDto, customOAuth2User.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(watchHistoryDto);

    }

//    @PatchMapping("api/videos/{video_id}")
//    public ResponseEntity<VideoDto> updateWatchHistory(@PathVariable Long videoId, @RequestBody WatchHistoryDto watchHistoryDto) {
//        log.info(videoId.toString());
//        watchHistoryService.

        // video 조회수 증가
//        try {
//            return ResponseEntity.status(HttpStatus.OK).body(videoService.increaseViews(videoId));
//        } catch (Exception e) {
//            log.info(e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//    }

//    @PostMapping("api/videos/test/new")
//    public VideoDto testNewVideo(@RequestBody VideoDto videoDto) {
//        log.info("controller, post mapping to /videos/test/new" + videoDto.toString());
//        return videoService.testNewVideo(videoDto);
//    }
}
