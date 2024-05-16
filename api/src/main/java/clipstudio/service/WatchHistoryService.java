package clipstudio.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import clipstudio.Entity.Advertisement;
import clipstudio.Entity.Video;
import clipstudio.Entity.WatchHistory;
import clipstudio.dto.video.VideoPlayDto;
import clipstudio.dto.history.WatchHistoryDto;
import clipstudio.oauth2.User.User;
import clipstudio.oauth2.User.userRepository.UserRepository;
import clipstudio.repository.AdvertisementRepository;
import clipstudio.repository.VideoRepository;
import clipstudio.repository.WatchHistoryRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class WatchHistoryService {
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final WatchHistoryRepository watchHistoryRepository;
    private final AdvertisementRepository advertisementRepository;

//    public Video increaseVideoViewsAndSave(Video video) {
//        video.setTempDailyViews(video.getTempDailyViews()+1);
//        log.info("daily views of video: " + video.getTempDailyViews());
//        return videoRepository.save(video);
//    }
//
//    public boolean validateVideoStoppedTime(Video video, int videoStoppedTime) {
//        return video.getDurationSec() >= videoStoppedTime;
//    }
//
//    /** 재생 시점에 따른 광고 조회 여부 확인, 필요시 조회수 1 증가 */
//    public void updateAdvertisementViews(int lastVideoStoppedTime, long videoNumber, int videoStoppedTime, int videoDurationSec) {
//        int currOrder = lastVideoStoppedTime / 300 + 1; // 이미 조회수가 카운트 된 기존 재생 광고는 제외, 새로운 카운팅 시점 설정
//        int endBound = videoDurationSec - 3; // 동영상 길이 -3초까지만 광고가 붙음
//        while (currOrder <= Math.min(videoStoppedTime / 300, endBound)) { // 재생된 광고 순회하기
//            Advertisement advertisement = advertisementRepository.findByVideoNumberAndOrderInVideo(videoNumber, currOrder).orElseThrow();
//            advertisement.setTempDailyViews(advertisement.getTempDailyViews()+1); // 하루 동안 유효한 임시 광고 조회수 1 증가
//            Advertisement updated = advertisementRepository.save(advertisement); // 업데이트한 광고 저장
//            log.info(currOrder + "th advertisement of video number " + videoNumber + ", daily views: " + updated.getTempDailyViews());
//            currOrder += 1;
//        }
//    }
//
//    @Transactional
//    public WatchHistoryDto playVideo(@PathVariable Long videoNumber,
//                                     @RequestBody VideoPlayDto videoPlayDto,
//                                     String userEmail) {
//        log.info("inside watch history service, playVideo");
//        User user = userRepository.findByEmail(userEmail).orElseThrow();
//        Video video = videoRepository.findByNumber(videoNumber).orElseThrow();
//        int videoStoppedTime = videoPlayDto.getVideoStoppedTime(); // 어디까지 재생했는지 확인
//        if (!validateVideoStoppedTime(video, videoStoppedTime)) { // 동영상 길이보다 큰 숫자의 재생 멈춤 시점이 들어올 경우, 동영상 시청 시간을 초기화해서 리턴
//            return WatchHistoryDto.builder()
//                    .videoNumber(videoNumber)
//                    .userEmail(user.getEmail())
//                    .videoStoppedTime(videoStoppedTime).build();
//        }
//        WatchHistory prevWatchHistory = watchHistoryRepository
//                .findByUserEmailAndVideoNumber(user.getEmail(), videoNumber)
//                .orElse(null);
//        if (prevWatchHistory == null) { // 이전 시청 기록이 없다면 시청 기록 새로 만들기
//            log.info("No watch history, generating a new one...");
//            video = increaseVideoViewsAndSave(video); // 동영상 조회수 1 증가시켜 저장
//            updateAdvertisementViews(0, videoNumber, videoStoppedTime, video.getDurationSec()); // 재생 시점에 따른 광고 조회 여부 확인, 필요시 조회수 1 증가
//            WatchHistory created = new WatchHistory(user, video, videoStoppedTime);
//            return WatchHistoryDto.fromEntity(watchHistoryRepository.save(created)); // 엔티티 생성, 시청기록 리파지토리에 저장 요청
//        }
//        log.info("watch history exists, updating previous watch history"); // 이전 시청 기록이 있다면 기존 것을 업데이트
//        int prevVideoStoppedTime = prevWatchHistory.getVideoStoppedTimeSec(); // 이전에 어디까지 재생했었는지 확인
//        prevWatchHistory.setVideoStoppedTimeSec(videoStoppedTime); // 새로 들어온 재생 시점으로 기존 시청 기록 업데이트
//        updateAdvertisementViews(prevVideoStoppedTime, videoNumber, videoStoppedTime, video.getDurationSec()); // 재생 시점에 따른 광고 조회 여부 확인, 필요시 조회수 1 증가
//        return WatchHistoryDto.fromEntity(watchHistoryRepository.save(prevWatchHistory)); // 새로운 정보로 엔티티 생성, 시청기록 리파지토리에 저장 요청
//    }
}
