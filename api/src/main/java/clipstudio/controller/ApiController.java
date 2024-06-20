package clipstudio.controller;

import java.time.LocalDate;

import clipstudio.dto.profit.DailyProfitDto;
import clipstudio.dto.profit.ProfitByPeriodDto;
import clipstudio.dto.stastistics.Top5ViewsByPeriod;
import clipstudio.dto.stastistics.Top5ViewsDaily;
import clipstudio.dto.video.VideoUploadDto;
import clipstudio.dto.video.VideoDto;
import clipstudio.service.ProfitService;
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
@RequiredArgsConstructor
@Slf4j
public class ApiController {

    private final VideoService videoService;
    private final ProfitService profitService;

    @PostMapping("/api/videos")
    public ResponseEntity<VideoDto> uploadVideo(@RequestBody VideoUploadDto videoUploadDto,
                                                @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
           try {
               return ResponseEntity.status(HttpStatus.OK).body(
                       videoService.uploadVideo(videoUploadDto, customOAuth2User.getEmail()));
           } catch (Exception e) {
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
           }
    }

    @PostMapping("/api/player/{videoNumber}")
    public ResponseEntity<WatchHistoryDto> playVideo(@PathVariable Long videoNumber,
                                                     @RequestBody VideoPlayDto videoPlayDto,
                                                     @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    videoService.playVideo(videoNumber, videoPlayDto, customOAuth2User.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/api/profit/day/{date}") // 일일 수익 조회
    public ResponseEntity<DailyProfitDto> showDailyProfit(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                                @PathVariable String date) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    profitService.showDailyProfit(customOAuth2User.getEmail(), LocalDate.parse(date)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/api/profit/today") // 오늘 수익 조회
    public ResponseEntity<DailyProfitDto> showTodayProfit(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    profitService.showDailyProfit(customOAuth2User.getEmail(), LocalDate.now()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/api/profit/week/{startDate}") // 주간 수익 조회
    public ResponseEntity<ProfitByPeriodDto> showWeeklyProfit(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                              @PathVariable String startDate) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    profitService.showWeeklyProfit(customOAuth2User.getEmail(), LocalDate.parse(startDate)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/api/profit/month/{startDate}") // 월별 수익 조회
    public ResponseEntity<ProfitByPeriodDto> showMonthlyProfit(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                               @PathVariable String startDate) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    profitService.showMonthlyProfit(customOAuth2User.getEmail(), LocalDate.parse(startDate)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/api/profit/year/{startDate}") // 연 광고 수익 정산금 조회
    public ResponseEntity<ProfitByPeriodDto> showYearlyProfit(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                                 @PathVariable String startDate) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    profitService.showYearlyProfit(customOAuth2User.getEmail(), LocalDate.parse(startDate)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/api/top5/views/day/{date}")
    public ResponseEntity<Top5ViewsDaily> showDailyTop5Views(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                             @PathVariable String date) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(profitService.showDailyTop5Views("cyqw@poakoi.com", LocalDate.parse(date)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/api/top5/views/week/{date}")
    public ResponseEntity<Top5ViewsByPeriod> showWeeklyTop5Views(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                             @PathVariable String date) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(profitService.showWeeklyTop5Views("cyqw@poakoi.com", LocalDate.parse(date)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
