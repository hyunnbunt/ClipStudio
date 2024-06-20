package clipstudio.controller;

import java.time.LocalDate;

import clipstudio.dto.profit.DailyProfitDto;
import clipstudio.dto.profit.ProfitByPeriodDto;
import clipstudio.dto.stastistics.Top5ViewsByPeriod;
import clipstudio.dto.stastistics.Top5ViewsDaily;
import clipstudio.dto.video.VideoUploadDto;
import clipstudio.dto.video.VideoDto;
import clipstudio.service.DailyProfitService;
import clipstudio.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import clipstudio.dto.video.VideoPlayDto;
import clipstudio.dto.history.WatchHistoryDto;
import clipstudio.oauth2.User.oauth2.CustomOAuth2User;

@RestController
@Slf4j
@RequestMapping("/")
@RequiredArgsConstructor
public class ApiController {

    // @RequiredArgsConstructor -> final field 생성자 주입
    private final VideoService videoService;
    private final DailyProfitService dailyProfitService;

    // 동영상 업로드
    @PostMapping("/api/videos")
    public ResponseEntity<VideoDto> uploadVideo(@RequestBody VideoUploadDto videoUploadDto,
                                                @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
            log.info("video upload requested");
           try {
               return ResponseEntity.status(HttpStatus.OK).body(videoService.uploadVideo(videoUploadDto, customOAuth2User.getEmail()));
           } catch (Exception e) {
               log.info(e.getMessage());
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
           }
    }

    // 동영상 재생, 접속한 사용자의 동영상 시청 기록 생성 또는 업데이트
    @PostMapping("/api/player/{videoNumber}")
    public ResponseEntity<WatchHistoryDto> playVideo(@PathVariable Long videoNumber,
                                                     @RequestBody VideoPlayDto videoPlayDto,
                                                     @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        WatchHistoryDto updatedDto = videoService.playVideo(videoNumber, videoPlayDto, customOAuth2User.getEmail());
        try {
            return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 일일 광고 수익 정산금 조회
    @GetMapping("/api/profit/day/{date}")
    public ResponseEntity<DailyProfitDto> showDailyProfit(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                                @PathVariable String date) {
//        if (!customOAuth2User.getRole().equals(Role.seller)) {
//            throw new Exception();
//        }
        log.info(customOAuth2User.getEmail());
        try {
            return ResponseEntity.status(HttpStatus.OK).body(dailyProfitService.showDailyProfit("cyqw@poakoi.com", LocalDate.parse(date)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 주간 광고 수익 정산금 조회
    @GetMapping("/api/profit/week/{date}")
    public ResponseEntity<ProfitByPeriodDto> showWeeklyProfit(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                              @PathVariable String date) {
        log.info(customOAuth2User.getEmail());
        try {
            return ResponseEntity.status(HttpStatus.OK).body(dailyProfitService.showWeeklyProfit(customOAuth2User.getEmail(), LocalDate.parse(date)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
/**
    // 월별 광고 수익 정산금 조회
    @GetMapping("/api/profit/month/{date}")
    public ResponseEntity<ProfitByPeriodDto> showMonthlyProfit(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                               @PathVariable String date) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(dailyProfitService.showMonthlyProfit("cyqw@poakoi.com", LocalDate.parse(date)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
//
//    // 연 광고 수익 정산금 조회
    @GetMapping("/api/profit/year/{date}")
    public ResponseEntity<ProfitByPeriodDto> showYearlyProfit(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                                 @PathVariable String date) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(dailyProfitService.showYearlyProfit("cyqw@poakoi.com", LocalDate.parse(date)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
*/
    @GetMapping("/api/top5/views/day/{date}")
    public ResponseEntity<Top5ViewsDaily> showDailyTop5Views(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                             @PathVariable String date) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(dailyProfitService.showDailyTop5Views("cyqw@poakoi.com", LocalDate.parse(date)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/api/top5/views/week/{date}")
    public ResponseEntity<Top5ViewsByPeriod> showWeeklyTop5Views(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                             @PathVariable String date) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(dailyProfitService.showWeeklyTop5Views("cyqw@poakoi.com", LocalDate.parse(date)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
