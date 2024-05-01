package projects.seller.ClipStudio.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import projects.seller.ClipStudio.Entity.Advertisement;
import projects.seller.ClipStudio.Entity.Video;
import projects.seller.ClipStudio.Entity.WatchHistory;
import projects.seller.ClipStudio.dto.PlayVideoDto;
import projects.seller.ClipStudio.dto.WatchHistoryDto;
import projects.seller.ClipStudio.oauth2.User.entity.User;
import projects.seller.ClipStudio.oauth2.User.userRepository.UserRepository;
import projects.seller.ClipStudio.repository.AdvertisementRepository;
import projects.seller.ClipStudio.repository.VideoRepository;
import projects.seller.ClipStudio.repository.WatchHistoryRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class WatchHistoryService {
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final WatchHistoryRepository watchHistoryRepository;
    private final AdvertisementRepository advertisementRepository;
    private final VideoService videoService;

    public Video increaseVideoViewsAndSave(Video video) {
        video.setDailyViews(video.getDailyViews()+1);
        return videoRepository.save(video);
    }

    public boolean validateVideoStoppedTime(Video video, int videoStoppedTime) {
        return video.getDuration() >= videoStoppedTime;
    }

    public void updateAdvertisementViews(int lastVideoStoppedTime, long videoNumber, int videoStoppedTime) {
        // 먼저 해야 함.
        int tempOrder = (lastVideoStoppedTime - 3) / 300 + 1;
        while (tempOrder <= (videoStoppedTime-3) / 300) {
            log.info(String.valueOf(tempOrder));
            Advertisement advertisement = advertisementRepository.findByVideoNumberAndOrderInVideo(videoNumber, tempOrder).orElseThrow();
            advertisement.setDailyViews(advertisement.getDailyViews()+1);
            advertisementRepository.save(advertisement);
            tempOrder += 1;
        }
    }

    @Transactional
    public WatchHistoryDto playVideo(@PathVariable Long videoNumber,
                                     @RequestBody PlayVideoDto playVideoDto,
                                     String userEmail) {
        log.info("inside watch history service");
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        Video video = videoRepository.findByNumber(videoNumber).orElseThrow();
        int videoStoppedTime = playVideoDto.getVideoStopped();
        if (!validateVideoStoppedTime(video, videoStoppedTime)) {
            throw new IllegalArgumentException();
        };
        WatchHistory prevWatchHistory = watchHistoryRepository
                .findByUserEmailAndVideoNumber(user.getEmail(), videoNumber)
                .orElse(null);
        if (prevWatchHistory == null) {
            log.info("No watch history, generating a new one...");
            increaseVideoViewsAndSave(video);
            updateAdvertisementViews(0, videoNumber, videoStoppedTime);
            WatchHistory created = new WatchHistory(user, video, videoStoppedTime);
            return WatchHistoryDto.fromEntity(watchHistoryRepository.save(created));
        }
        log.info("watch history exists, updating previous watch history");
        int prevVideoStoppedTime = prevWatchHistory.getVideoStoppedTime();
        updateAdvertisementViews(prevVideoStoppedTime, videoNumber, videoStoppedTime);
        prevWatchHistory.setVideoStoppedTime(videoStoppedTime);
        return WatchHistoryDto.fromEntity(watchHistoryRepository.save(prevWatchHistory));
    }
}
