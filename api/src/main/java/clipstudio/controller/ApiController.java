package clipstudio.controller;

import java.time.LocalDate;
import java.util.List;

import clipstudio.dto.profit.ProfitDto;
import clipstudio.dto.profit.ProfitByPeriodDto;
import clipstudio.dto.stastistics.Top5ViewsByPeriod;
import clipstudio.dto.stastistics.Top5Views;
import clipstudio.dto.video.VideoUploadDto;
import clipstudio.dto.video.VideoDto;
import clipstudio.oauth2.User.userinfo.GoogleOAuth2UserInfo;
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

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApiController {

    private final VideoService videoService;
    private final ProfitService profitService;

    @GetMapping("/api/videos")
    public ResponseEntity<List<VideoDto>> showVideos(@AuthenticationPrincipal GoogleOAuth2UserInfo.CustomOAuth2User customOAuth2User) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    videoService.showVideos(customOAuth2User.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/api/videos")
    public ResponseEntity<VideoDto> uploadVideo(@RequestBody VideoUploadDto videoUploadDto,
                                                @AuthenticationPrincipal GoogleOAuth2UserInfo.CustomOAuth2User customOAuth2User) {
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
                                                     @AuthenticationPrincipal GoogleOAuth2UserInfo.CustomOAuth2User customOAuth2User) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    videoService.playVideo(videoNumber, videoPlayDto, customOAuth2User.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/api/profit/day/{date}") // 일일 수익 조회
    public ResponseEntity<ProfitByPeriodDto> showDailyProfit(@AuthenticationPrincipal GoogleOAuth2UserInfo.CustomOAuth2User customOAuth2User,
                                                     @PathVariable String date) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    profitService.showProfit(customOAuth2User.getEmail(), LocalDate.parse(date)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/api/profit/today") // 오늘 수익 조회
    public ResponseEntity<ProfitByPeriodDto> showTodayProfit(@AuthenticationPrincipal GoogleOAuth2UserInfo.CustomOAuth2User customOAuth2User) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    profitService.showProfit(customOAuth2User.getEmail(), LocalDate.now()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/api/profit/week/{startDate}") // 주간 수익 조회
    public ResponseEntity<ProfitByPeriodDto> showWeeklyProfit(@AuthenticationPrincipal GoogleOAuth2UserInfo.CustomOAuth2User customOAuth2User,
                                                              @PathVariable String startDate) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    profitService.showWeeklyProfit(customOAuth2User.getEmail(), LocalDate.parse(startDate)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/api/profit/month/{startDate}") // 월별 수익 조회
    public ResponseEntity<ProfitByPeriodDto> showMonthlyProfit(@AuthenticationPrincipal GoogleOAuth2UserInfo.CustomOAuth2User customOAuth2User,
                                                               @PathVariable String startDate) {
        String testEmail = "uzmphr@zzzjp.com";
        log.info(testEmail);
        long start = System.currentTimeMillis();
        ProfitByPeriodDto profitByPeriodDto = profitService.showMonthlyProfit(testEmail, LocalDate.parse(startDate));
        long executionTime = System.currentTimeMillis() - start;
        log.info("monthly profit reading executed in " + executionTime + "ms");
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
//                    profitService.showMonthlyProfit(customOAuth2User.getEmail(), LocalDate.parse(startDate)));
//                    profitService.showMonthlyProfit(testEmail, LocalDate.parse(startDate)));
                    profitByPeriodDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/api/profit/year/{startDate}") // 연 광고 수익 정산금 조회
    public ResponseEntity<ProfitByPeriodDto> showYearlyProfit(@AuthenticationPrincipal GoogleOAuth2UserInfo.CustomOAuth2User customOAuth2User,
                                                                 @PathVariable String startDate) {
        long start = System.currentTimeMillis();
        ProfitByPeriodDto profitByPeriodDto = profitService.showYearlyProfit(customOAuth2User.getEmail(), LocalDate.parse(startDate));
        long executionTime = System.currentTimeMillis() - start;
        log.info("ExecutionTime: " + executionTime + "ms");
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
//                    profitService.showYearlyProfit(customOAuth2User.getEmail(), LocalDate.parse(startDate)));
            profitByPeriodDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/api/top5/views/day/{date}")
    public ResponseEntity<Top5Views> showDailyTop5Views(@AuthenticationPrincipal GoogleOAuth2UserInfo.CustomOAuth2User customOAuth2User,
                                                        @PathVariable String date) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    profitService.showTop5Views(customOAuth2User.getEmail(), LocalDate.parse(date)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/api/top5/views/week/{date}")
    public ResponseEntity<Top5ViewsByPeriod> showWeeklyTop5Views(@AuthenticationPrincipal GoogleOAuth2UserInfo.CustomOAuth2User customOAuth2User,
                                                             @PathVariable String date) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    profitService.showWeeklyTop5Views(customOAuth2User.getEmail(), LocalDate.parse(date)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/api/top5/views/month/{date}")
    public ResponseEntity<Top5ViewsByPeriod> showMonthlyTop5Views(@AuthenticationPrincipal GoogleOAuth2UserInfo.CustomOAuth2User customOAuth2User,
                                                                 @PathVariable String date) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    profitService.showMonthlyTop5Views(customOAuth2User.getEmail(), LocalDate.parse(date)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/api/top5/views/year/{date}")
    public ResponseEntity<Top5ViewsByPeriod> showYearlyTop5Views(@AuthenticationPrincipal GoogleOAuth2UserInfo.CustomOAuth2User customOAuth2User,
                                                                  @PathVariable String date) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    profitService.showYearlyTop5Views(customOAuth2User.getEmail(), LocalDate.parse(date)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
