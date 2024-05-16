package clipstudio.controller;

import clipstudio.Entity.daily.DailyProfitOfVideo;
import java.time.LocalDate;

import clipstudio.dto.DailyProfitDto;
import clipstudio.dto.profit.TotalAdvertisementsProfitOfVideoDto;
import clipstudio.dto.video.VideoUploadDto;
import clipstudio.dto.video.VideoDto;
import clipstudio.service.DailyProfitOfVideoService;
import clipstudio.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import clipstudio.dto.video.PlayVideoDto;
import clipstudio.dto.history.WatchHistoryDto;
import clipstudio.oauth2.User.oauth2.CustomOAuth2User;
import clipstudio.service.WatchHistoryService;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/")
@RequiredArgsConstructor
public class ApiController {

    // @RequiredArgsConstructor -> final field 생성자 주입
    private final WatchHistoryService watchHistoryService;
    private final DailyProfitOfVideoService dailyProfitOfVideoService;
    private final VideoService videoService;

    // 동영상 업로드
    @PostMapping("/api/videos/new")
    public ResponseEntity<VideoDto> uploadVideo(@RequestBody VideoUploadDto videoUploadDto,
                                                @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.status(HttpStatus.OK).body(videoService.uploadVideo(videoUploadDto, customOAuth2User.getEmail()));
    }

    // 동영상 재생, 접속한 사용자의 동영상 시청 기록 생성 또는 업데이트
    @PostMapping("/api/videos/player/{videoNumber}")
    public ResponseEntity<WatchHistoryDto> playVideo(@PathVariable Long videoNumber,
                                                     @RequestBody PlayVideoDto playVideoDto,
                                                     @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        WatchHistoryDto updatedDto = watchHistoryService.playVideo(videoNumber, playVideoDto, customOAuth2User.getEmail());
        try {
            return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 일일 동영상 수익 정산금 조회
    @GetMapping("/api/profit/videos")
    public ResponseEntity<List<DailyProfitOfVideo>> showDailyProfitOfVideos(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                                            LocalDate date) {
//        if (!customOAuth2User.getRole().equals(Role.seller)) {
//            throw new Exception();
//        }
        log.info(customOAuth2User.getEmail());
        try {
            return ResponseEntity.status(HttpStatus.OK).body(dailyProfitOfVideoService.showDailyProfitOfVideos(customOAuth2User.getEmail(), date));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 일일 광고 수익 정산금 조회
    @GetMapping("/api/profit/advertisements")
    public ResponseEntity<List<TotalAdvertisementsProfitOfVideoDto>> showDailyProfitOfAdvertisements(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                                                                     LocalDate date) {
//        if (!customOAuth2User.getRole().equals(Role.seller)) {
//            throw new Exception();
//        }
        log.info(customOAuth2User.getEmail());
        try {
            return ResponseEntity.status(HttpStatus.OK).body(dailyProfitOfVideoService.showDailyProfitOfAdvertisements(customOAuth2User.getEmail(), date));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 일일 광고 수익 정산금 조회
    @GetMapping("/api/profit")
    public ResponseEntity<List<DailyProfitDto>> showDailyProfit(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                                LocalDate date) {
//        if (!customOAuth2User.getRole().equals(Role.seller)) {
//            throw new Exception();
//        }
        log.info(customOAuth2User.getEmail());
        try {
            return ResponseEntity.status(HttpStatus.OK).body(dailyProfitOfVideoService.showDailyProfit(customOAuth2User.getEmail(), date));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}