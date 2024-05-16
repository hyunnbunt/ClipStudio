package clipstudio.service;

import clipstudio.Entity.Video;
import clipstudio.Entity.daily.DailyProfitOfVideo;
import clipstudio.oauth2.User.User;
import clipstudio.oauth2.User.userRepository.UserRepository;
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
    private final DailyProfitOfVideoRepository dailyProfitOfVideoRepository;
    public List<DailyProfitOfVideo> showDailyProfitOf(String userEmail, LocalDate date) throws IllegalArgumentException {
        log.info("inside DailyProfitOfVideoService");
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        List<Video> videoList = videoRepository.findByUploader(user).orElseThrow(); // 사용자가 업로드한 모든 동영상 목록 불러오기
        if (videoList.isEmpty()) {
            log.info("You don't have any uploaded video.");
            throw new IllegalArgumentException();
        }
        log.info(String.valueOf(videoList.size()));
        List<DailyProfitOfVideo> resultList = new LinkedList<>(); // 동영상 각각의 해당 날짜 정산 내역을 찾아 목록 생성
        for (Video video : videoList) {
            // 해당 날짜에 정산 내역이 있는 동영상만 목록에 추가
            dailyProfitOfVideoRepository.findByVideoNumberAndCalculatedDate(video.getNumber(), date).ifPresent(resultList::add);
        }
        if (resultList.isEmpty()) {
            log.info("Can't find your profit statistic on " + date.toString());
        }
        return resultList;
    }

}
