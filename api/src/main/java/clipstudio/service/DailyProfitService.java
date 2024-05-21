package clipstudio.service;

import clipstudio.Entity.Video;
import clipstudio.Entity.batch.daily.VideoDailyProfit;
import clipstudio.Entity.batch.daily.VideoDailyProfitKey;
import clipstudio.dto.profit.DailyProfitDto;
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
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyProfitService {
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final AdvertisementRepository advertisementRepository;
    private final DailyProfitOfAdvertisementRepository dailyProfitOfAdvertisementRepository;
    private final DailyProfitOfVideoRepository dailyProfitOfVideoRepository;

    public List<DailyProfitDto> showDailyProfit(String userEmail, LocalDate date) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        List<Video> videoList = videoRepository.findByUploader(user).orElse(null); // 사용자가 업로드한 모든 동영상 목록 불러오기
        // 사용자의 모든 동영상 수익 목록 생성
        List<DailyProfitDto> dailyProfitDtoList = new LinkedList<>();
        for (Video video : videoList) {
            // 각 동영상별 일일 동영상 및 광고 정산 금액 dto 생성
            DailyProfitDto dailyProfitDto = new DailyProfitDto();
            // 일일 동영상 수익 필드 업데이트
            VideoDailyProfit videoDailyProfit = dailyProfitOfVideoRepository.findById(new VideoDailyProfitKey(video.getNumber(), date)).orElse(null);
            if (videoDailyProfit != null) {
                dailyProfitDto.setProfitOfVideo(videoDailyProfit.getDailyProfitOfVideo());
            }
            // 일일 광고 수익 필드 업데이트
            dailyProfitDto.setProfitOfAdvertisements(videoDailyProfit.getDailyTotalProfitOfAdvertisements());
            dailyProfitDto.setVideoNumber(videoDailyProfit.getVideoNumber());
            dailyProfitDto.setCalculatedDate(videoDailyProfit.getCalculatedDate());
            dailyProfitDto.setProfitTotal(videoDailyProfit.getDailyProfitOfVideo() + videoDailyProfit.getDailyTotalProfitOfAdvertisements());
            // 현재 동영상 수익을 반환 목록에 추가
            dailyProfitDtoList.add(dailyProfitDto);
        }
        return dailyProfitDtoList;
    }
}
