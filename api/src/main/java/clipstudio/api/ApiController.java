package clipstudio.api;

import clipstudio.Entity.VideoDailyHistory;

import java.util.Calendar;
import java.util.Date;

import clipstudio.dto.VideoUploadDto;
import clipstudio.dto.VideoDto;
import clipstudio.service.DailyProfitOfVideoService;
import clipstudio.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import clipstudio.dto.PlayVideoDto;
import clipstudio.dto.WatchHistoryDto;
import clipstudio.oauth2.User.oauth2.CustomOAuth2User;
import clipstudio.service.WatchHistoryService;

import java.util.GregorianCalendar;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/")
@RequiredArgsConstructor
public class ApiController {

    private final WatchHistoryService watchHistoryService;
    private final DailyProfitOfVideoService dailyProfitOfVideoService;
    private final VideoService videoService;

    // 비디오 재생
    @PostMapping("/api/videos/{videoNumber}")
    public ResponseEntity<WatchHistoryDto> playVideo(@PathVariable Long videoNumber,
                                                     @RequestBody PlayVideoDto playVideoDto,
                                                     @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        WatchHistoryDto updatedDto = watchHistoryService.playVideo(videoNumber, playVideoDto, customOAuth2User.getEmail());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 동영상 업로드
    @PostMapping("/api/videos/new")
    public ResponseEntity<VideoDto> uploadVideo(@RequestBody VideoUploadDto videoUploadDto,
                                                @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.status(HttpStatus.OK).body(videoService.uploadVideo(videoUploadDto, customOAuth2User.getEmail()));
    }

    // 수익 정산금 조회
    @GetMapping("/api/videos")
    public ResponseEntity<List<VideoDailyHistory>> showDailyProfitOfAllVideos(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) throws Exception {
//        if (!customOAuth2User.getRole().equals(Role.seller)) {
//            throw new Exception();
//        }
        log.info(customOAuth2User.getEmail());
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
//        calendar.add(Calendar.DATE, -1);
//        dailyProfitOfVideoService.showDailyProfitOfAllVideos(customOAuth2User.getEmail(), calendar.getTime());
//        log.info(calendar.getTime().toString());
//        log.info(String.valueOf(dailyProfitOfVideoService.showDailyProfitOfAllVideos(customOAuth2User.getEmail(), calendar.getTime()).getDailyViews()));
        try {
            return ResponseEntity.status(HttpStatus.OK).body(dailyProfitOfVideoService.showDailyProfitOfAllVideos(customOAuth2User.getEmail(), calendar.getTime()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 어떤 유저가 비디오를 재생했을 때, 이미 히스토리가 있으면 있는 걸 보내주고, 없으면 새로 만들어서 보내줄 것.
//    @GetMapping("/api/videos/{videoNumber}")
//    public WatchHistoryDto playVideo(@PathVariable long videoNumber) {
//        log.info(String.valueOf(videoNumber));
//        return new WatchHistoryDto();
//    }



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
