package clipstudio.service;

import clipstudio.Entity.Video;
import clipstudio.Entity.VideoDailyHistory;
import clipstudio.dto.DailyProfitOfVideoDto;
import clipstudio.oauth2.User.User;
import clipstudio.oauth2.User.userRepository.UserRepository;
import clipstudio.repository.AdvertisementDailyHistoryRepository;
import clipstudio.repository.VideoDailyHistoryRepository;
import clipstudio.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public List<VideoDailyHistory> showDailyProfitOfAllVideos(String userEmail, Date historyDate) throws ParseException {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
//        log.info(user.getEmail());
        List<Video> videoList = videoRepository.findByUploader(user).orElseThrow();
        List<VideoDailyHistory> resultList = new LinkedList<>();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(historyDate);
        Date testDate = formatter.parse(date);
        log.info(testDate.toString());
        for (Video video : videoList) {
            resultList
                    .add(videoDailyHistoryRepository.findByVideoNumberAndCalculatedDate(video.getNumber(), testDate)
                    .orElseThrow());
        }
        return resultList;
    }

}
