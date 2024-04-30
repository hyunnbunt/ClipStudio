package projects.seller.ClipStudio.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import projects.seller.ClipStudio.Entity.AdIn;
import projects.seller.ClipStudio.Entity.Video;
import projects.seller.ClipStudio.Entity.WatchHistory;
import projects.seller.ClipStudio.dto.VideoStoppedTimeDto;
import projects.seller.ClipStudio.dto.WatchHistoryDto;
import projects.seller.ClipStudio.oauth2.User.entity.User;
import projects.seller.ClipStudio.oauth2.User.userRepository.UserRepository;
import projects.seller.ClipStudio.repository.AdInRepository;
import projects.seller.ClipStudio.repository.VideoRepository;
import projects.seller.ClipStudio.repository.WatchHistoryRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class WatchHistoryService {
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final WatchHistoryRepository watchHistoryRepository;
    private final AdInRepository adInRepository;
    private final VideoService videoService;

    public WatchHistory createWatchHistory(User user, Video video, int videoStoppedTime) {
        log.info("no watch history, generating new one...");
        video.setViews(video.getViews()+1);
        videoRepository.save(video);
        log.info("updated views : " + video.getViews());
        WatchHistory created = new WatchHistory(user, video, videoStoppedTime);
        log.info(created.toString());
        return watchHistoryRepository.save(created);
    }
    public WatchHistoryDto updateWatchHistory(@PathVariable Long videoNumber,
                                   @RequestBody VideoStoppedTimeDto videoStoppedTimeDto,
                                   String userEmail) {
        log.info("inside watch history service");
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        Video video = videoRepository.findByNumber(videoNumber).orElseThrow();
        int videoStoppedTime = videoStoppedTimeDto.getVideoStoppedTime();
        WatchHistory prevWatchHistory = watchHistoryRepository
                .findByUserEmailAndVideoNumber(user.getEmail(), videoNumber)
                .orElse(null);
        if (prevWatchHistory == null) {
            WatchHistory created = createWatchHistory(user, video, videoStoppedTime);
//            log.info("no watch history, generating new one...");
//            video.setViews(video.getViews()+1);
//            videoRepository.save(video);
//            log.info(String.valueOf(video.getViews()));
//            WatchHistory created = new WatchHistory(user, video, videoStoppedTimeDto.getVideoStoppedTime());
//            log.info(created.toString());
            return WatchHistoryDto.fromEntity(created);
        } else {
            log.info("watch history exists, updating previous watch history");
            int prevVideoStoppedTime = prevWatchHistory.getVideoStoppedTime();
            int recentVideoStoppedTime = videoStoppedTimeDto.getVideoStoppedTime();
            for (int i = prevVideoStoppedTime; i < recentVideoStoppedTime; i ++) {
                if (i % 300 == 0) {
                    AdIn adIn = adInRepository.findByVideoNumberAndOrderInVideo(videoNumber, i/5).orElseThrow();
                    adIn.setViews(adIn.getViews()+1);
                    log.info(String.valueOf(adIn.getViews()));

                }
            }
            prevWatchHistory.setVideoStoppedTime(recentVideoStoppedTime);
            WatchHistory updated = watchHistoryRepository.save(prevWatchHistory);
            return WatchHistoryDto.fromEntity(updated);
        }
//        log.info("updating existing watch history...");
//        return WatchHistoryDto.fromEntity(prevWatchHistory);
    }
}
