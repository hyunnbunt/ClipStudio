package clipstudio.service;

import clipstudio.Entity.Advertisement;
import clipstudio.Entity.Video;
import clipstudio.Entity.daily.DailyProfitOfAdvertisement;
import clipstudio.Entity.daily.DailyProfitOfVideo;
import clipstudio.dto.DailyProfitDto;
import clipstudio.dto.profit.TotalAdvertisementsProfitOfVideoDto;
import clipstudio.oauth2.User.User;
import clipstudio.oauth2.User.userRepository.UserRepository;
import clipstudio.repository.AdvertisementRepository;
import clipstudio.repository.DailyProfitOfAdvertisementRepository;
import clipstudio.repository.DailyProfitOfVideoRepository;
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
public class DailyProfitOfVideoService {
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final AdvertisementRepository advertisementRepository;
    private final DailyProfitOfAdvertisementRepository dailyProfitOfAdvertisementRepository;
    private final DailyProfitOfVideoRepository dailyProfitOfVideoRepository;

    public List<DailyProfitDto> showDailyProfit(String userEmail, LocalDate date) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        List<Video> videoList = videoRepository.findByUploader(user).orElse(null); // 사용자가 업로드한 모든 동영상 목록 불러오기
        // 사용자가 업로드한 동영상이 없다면 exception 던지기
        if (videoList == null) {
            log.info("You don't have any uploaded video.");
            throw new IllegalArgumentException();
        }
        // 사용자의 모든 동영상 수익 정산 금액 dto 목록 생성
        List<DailyProfitDto> dailyProfitDtoList = new LinkedList<>();
        for (Video video : videoList) {
            // 각 동영상별 일일 동영상 및 광고 정산 금액 dto 생성
            DailyProfitDto dailyProfitDto = new DailyProfitDto();
            // 일일 동영상 수익 필드 업데이트
            DailyProfitOfVideo dailyProfitOfVideo = dailyProfitOfVideoRepository.findByVideoNumberAndCalculatedDate(video.getNumber(), date).orElse(null);
            if (dailyProfitOfVideo != null) {
                dailyProfitDto.setVideoProfit(dailyProfitOfVideo.getDailyProfit());
            }
            // 일일 광고 수익 필드 업데이트
            List<Advertisement> advertisementList = advertisementRepository
                    .findByVideoNumber(video.getNumber())
                    .orElse(null); // 사용자 업로드 동영상별 광고 목록 가져오기
            if (advertisementList != null) { // 광고 목록이 존재한다면, 광고 목록을 순회하며, 각 광고 넘버와 조회 날짜로 광고 정산 내역을 조회할 것
                double totalAdvertisementsProfit = 0;
                for (Advertisement advertisement : advertisementList) {
                    DailyProfitOfAdvertisement dailyProfitOfAdvertisement = dailyProfitOfAdvertisementRepository.
                            findByAdvertisementNumberAndCalculatedDate(advertisement.getNumber(), date); // 각 광고별 정산 내역을 조회
                    totalAdvertisementsProfit += dailyProfitOfAdvertisement.getDailyProfit(); // 각 광고별 정산 금액을 합산
                }
                dailyProfitDto.setTotalProfitOfAllAdvertisementsInVideo(totalAdvertisementsProfit);
            }
            // 각 동영상별 일일 동영상 및 광고 정산 금액 dto를 목록에 추가
            dailyProfitDtoList.add(dailyProfitDto);
        }
        return dailyProfitDtoList;
    }
}
