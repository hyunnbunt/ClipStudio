package clipstudio.service;

import clipstudio.Entity.Video;
import clipstudio.Entity.batch.daily.VideoDailyProfit;
import clipstudio.Entity.batch.daily.VideoDailyProfitKey;
import clipstudio.dto.profit.DailyProfitDto;
import clipstudio.dto.profit.ProfitDto;
import clipstudio.dto.profit.WeeklyProfitDto;
import clipstudio.oauth2.User.User;
import clipstudio.oauth2.User.userRepository.UserRepository;
import clipstudio.repository.AdvertisementRepository;
import clipstudio.repository.batch.DailyProfitOfAdvertisementRepository;
import clipstudio.repository.batch.DailyProfitOfVideoRepository;
import clipstudio.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyProfitService {
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final DailyProfitOfVideoRepository dailyProfitOfVideoRepository;

    public DailyProfitDto showDailyProfit(String userEmail, LocalDate date) {
        log.info("inside showDailyProfit");
        log.info("date" + date.toString());
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        List<Video> videoList = videoRepository.findByUploader(user).orElse(null); // 사용자가 업로드한 모든 동영상 목록 불러오기
        if (videoList == null) {
            log.info("videoList is null.");
            return null;
        }
        log.info(String.valueOf(videoList.size()));
        DailyProfitDto dailyProfitDto = new DailyProfitDto();
        List<ProfitDto> list = new ArrayList<>();
        for (Video video : videoList) {
            // 각 동영상별 일일 동영상 및 광고 정산 금액 dto 생성
            ProfitDto profitDto = new ProfitDto();
            // 일일 동영상 수익 필드 업데이트
            VideoDailyProfit videoDailyProfit = dailyProfitOfVideoRepository.findById(new VideoDailyProfitKey(video.getNumber(), date)).orElse(null);
            if (videoDailyProfit != null) {
                profitDto.setProfitOfVideo(videoDailyProfit.getDailyProfitOfVideo());
                profitDto.setVideoNumber(videoDailyProfit.getVideoNumber());
                profitDto.setProfitTotal(videoDailyProfit.getDailyProfitOfVideo() + videoDailyProfit.getDailyTotalProfitOfAdvertisements());
                // 일일 광고 수익 필드 업데이트
                profitDto.setProfitOfAdvertisements(videoDailyProfit.getDailyTotalProfitOfAdvertisements());
            } else {
                log.info("videoDailyProfit null");
                profitDto.setProfitTotal(0);
                profitDto.setProfitTotal(0);
                profitDto.setProfitOfAdvertisements(0);
                profitDto.setVideoNumber(videoDailyProfit.getVideoNumber());
            }
            // 현재 동영상 수익을 반환 목록에 추가
            list.add(profitDto);
        }
        dailyProfitDto.setDailyProfit(list);
        dailyProfitDto.setDate(date);
        return dailyProfitDto;
    }

    public WeeklyProfitDto showWeeklyProfit(String userEmail, LocalDate date) {
        WeeklyProfitDto weeklyProfitDto = new WeeklyProfitDto();
        weeklyProfitDto.setWeeklyProfit(new ArrayList<>());
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        log.info(user.getEmail());
        List<Video> videos = videoRepository.findByUploader(user).orElse(null);
        if (videos == null) {
            return null;
        }
        log.info(videos.size() + "");
        LocalDate monday = getClosestMondayBefore(date);
        weeklyProfitDto.setStartDate(monday);
        weeklyProfitDto.setEndDate(monday.plusDays(6));
        LocalDate currDate = monday;
        for (int i = 1; i <= 7; i ++) {
            DailyProfitDto dailyProfitDto = new DailyProfitDto();
            dailyProfitDto.setDailyProfit(new ArrayList<>());
            dailyProfitDto.setDate(currDate);
            for (Video video : videos) {
                dailyProfitOfVideoRepository.findById(new VideoDailyProfitKey(video.getNumber(), currDate)).ifPresent(videoDailyProfit -> {
                    ProfitDto curr = ProfitDto.builder()
                            .videoNumber(video.getNumber())
                            .profitTotal(videoDailyProfit.getDailyProfitOfVideo() + videoDailyProfit.getDailyTotalProfitOfAdvertisements())
                            .profitOfVideo(videoDailyProfit.getDailyProfitOfVideo())
                            .profitOfAdvertisements(videoDailyProfit.getDailyTotalProfitOfAdvertisements())
                            .build();
                    dailyProfitDto.getDailyProfit().add(curr);
                });
            }
            weeklyProfitDto.getWeeklyProfit().add(dailyProfitDto);
            currDate = monday.plusDays(i);
        }
        return weeklyProfitDto;
//        LocalDate curr = monSun[0];
//        for (int i = 1; i < 7; i ++) {
//            log.info("inside for loop: " + curr);
//            DailyProfitDto dailyProfitDto = showDailyProfit(userEmail, curr);
//            if (dailyProfitDto != null) {
//                dailyProfitDtoList.add(dailyProfitDto);
//            }
//            curr = monSun[0].plusDays(i);
//        }
//        for (LocalDate curr = monSun[0]; curr.isBefore(monSun[1]) || curr.isEqual(monSun[1]); curr = curr.plusDays(1)) {
//            log.info(curr.toString());
//            DailyProfitDto dailyProfitDto = this.showDailyProfit(userEmail, date);
//            dailyProfitDtoList.add(dailyProfitDto);
//        }
    }
    public LocalDate getClosestMondayBefore(LocalDate localDate) {
        int fromMon = localDate.getDayOfWeek().compareTo(DayOfWeek.MONDAY);
        return localDate.minusDays(fromMon);
    }
}
