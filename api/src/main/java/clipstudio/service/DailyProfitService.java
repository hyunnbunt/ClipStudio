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
import org.springframework.stereotype.Service;

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
        log.info(userEmail);
        log.info(date.toString());
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        List<Video> videoList = videoRepository.findByUploader(user).orElse(null); // 사용자가 업로드한 모든 동영상 목록 불러오기
        if (videoList == null) {
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
            }
            // 현재 동영상 수익을 반환 목록에 추가
            list.add(profitDto);
        }
        dailyProfitDto.setDailyProfit(list);
        dailyProfitDto.setDate(date);
        return dailyProfitDto;
    }

    public WeeklyProfitDto showWeeklyProfit(String userEmail, LocalDate date) {
        LocalDate[] monSun = getWeek(date);
        WeeklyProfitDto weeklyProfitDto = new WeeklyProfitDto();
        weeklyProfitDto.setStartDate(monSun[0]);
        weeklyProfitDto.setEndDate(monSun[1]);
        List<DailyProfitDto> dailyProfitDtoList = new ArrayList<>();
        for (LocalDate curr = monSun[0]; curr.isBefore(monSun[1]) || curr.isEqual(monSun[1]); curr = curr.plusDays(1)) {

        }
    }
    public LocalDate[] getWeek(LocalDate localDate) {
        LocalDate[] monSun = new LocalDate[2];
        log.info(localDate.getDayOfWeek().toString());
    }
}
