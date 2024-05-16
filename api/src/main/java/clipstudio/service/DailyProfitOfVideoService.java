package clipstudio.service;

import clipstudio.Entity.Video;
import clipstudio.Entity.daily.VideoDailyHistory;
import clipstudio.oauth2.User.User;
import clipstudio.oauth2.User.userRepository.UserRepository;
import clipstudio.repository.AdvertisementDailyHistoryRepository;
import clipstudio.repository.VideoDailyHistoryRepository;
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
    private final VideoDailyHistoryRepository videoDailyHistoryRepository;
    private final AdvertisementDailyHistoryRepository advertisementDailyHistoryRepository;
    public List<VideoDailyHistory> showDailyProfitOfAllVideos(String userEmail, LocalDate historyDate) {
        log.info("inside DailyProfitOfVideoService");
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        List<Video> videoList = videoRepository.findByUploader(user).orElseThrow();
        log.info(String.valueOf(videoList.size()));
        List<VideoDailyHistory> resultList = new LinkedList<>();
        for (Video video : videoList) {
            log.info(String.valueOf(video.getNumber()));
            VideoDailyHistory videoDailyHistory = videoDailyHistoryRepository.findByVideoNumberAndCalculatedDate(video.getNumber(), historyDate).orElse(null);
            if (videoDailyHistory == null) {
                log.info("no history for this video, number: " + video.getNumber());
            } else {
                resultList.add(videoDailyHistory);
            }
        }
        log.info("resultList size: " + resultList.size());
        return resultList;
    }

}
