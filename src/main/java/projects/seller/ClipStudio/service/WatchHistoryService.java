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
        video = increaseVideoViewsAndSave(video);
        log.info("updated views : " + video.getViews());
        WatchHistory created = new WatchHistory(user, video, videoStoppedTime);
        log.info(created.toString());
        return watchHistoryRepository.save(created);
    }
    public Video increaseVideoViewsAndSave(Video video) {
        video.setViews(video.getViews()+1);
        return videoRepository.save(video);
    }

    public boolean validateVideoStoppedTime(Video video, int videoStoppedTime) {
        return video.getDuration() >= videoStoppedTime;
    }

    public void updateAdInVideoViews(int lastVideoStoppedTime, long videoNumber, int videoStoppedTime) {
        // 먼저 해야 함.
        int tempOrder = (lastVideoStoppedTime - 3) / 300 + 1;
        while (tempOrder <= (videoStoppedTime-3) / 300) {
            log.info(String.valueOf(tempOrder));
            AdIn adIn = adInRepository.findByVideoNumberAndOrderInVideo(videoNumber, tempOrder).orElseThrow();
            adIn.setViews(adIn.getViews()+1);
            AdIn updated = adInRepository.save(adIn);
            log.info(tempOrder + "th adin view now: " + updated.getViews());
            tempOrder += 1;
        }
    }

    public WatchHistoryDto updateWatchHistory(@PathVariable Long videoNumber,
                                   @RequestBody VideoStoppedTimeDto videoStoppedTimeDto,
                                   String userEmail) {
        log.info("inside watch history service");
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        Video video = videoRepository.findByNumber(videoNumber).orElseThrow();
        int videoStoppedTime = videoStoppedTimeDto.getVideoStoppedTime();
        if (!validateVideoStoppedTime(video, videoStoppedTime)) {
            throw new IllegalArgumentException();
        };
        WatchHistory prevWatchHistory = watchHistoryRepository
                .findByUserEmailAndVideoNumber(user.getEmail(), videoNumber)
                .orElse(null);
        if (prevWatchHistory == null) {
            log.info("No watch history, generating a new one...");
            increaseVideoViewsAndSave(video);
            updateAdInVideoViews(0, videoNumber, videoStoppedTime);
            WatchHistory created = new WatchHistory(user, video, videoStoppedTime);
            return WatchHistoryDto.fromEntity(watchHistoryRepository.save(created));
        }
        log.info("watch history exists, updating previous watch history");
        int prevVideoStoppedTime = prevWatchHistory.getVideoStoppedTime();
        log.info(String.valueOf(prevVideoStoppedTime));
        updateAdInVideoViews(prevVideoStoppedTime, videoNumber, videoStoppedTime);
        prevWatchHistory.setVideoStoppedTime(videoStoppedTime);
        return WatchHistoryDto.fromEntity(watchHistoryRepository.save(prevWatchHistory));
    }
}
