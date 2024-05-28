package clipstudio.service;

import clipstudio.Entity.Video;
import clipstudio.Entity.batch.daily.VideoDailyProfit;
import clipstudio.Entity.batch.daily.VideoDailyProfitKey;
import clipstudio.dto.profit.DailyProfitDto;
import clipstudio.dto.profit.ProfitDto;
import clipstudio.dto.profit.ProfitByPeriodDto;
import clipstudio.dto.stastistics.Top5ViewsByPeriod;
import clipstudio.dto.stastistics.Top5ViewsDaily;
import clipstudio.dto.stastistics.ViewsByPeriod;
import clipstudio.oauth2.User.User;
import clipstudio.oauth2.User.userRepository.UserRepository;
import clipstudio.repository.batch.DailyProfitOfVideoRepository;
import clipstudio.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyProfitService {
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final DailyProfitOfVideoRepository dailyProfitOfVideoRepository;

    public DailyProfitDto showDailyProfit(String userEmail, LocalDate date) {
        log.info("inside showDailyProfit");
        log.info("date" + date.toString());
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        List<Video> videoList = videoRepository.findByUploader(user).orElse(null); // 사용자가 업로드한 모든 동영상 목록 불러오기
        if (videoList == null) {
            log.info("videoList is null.");
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
            } else {
                log.info("videoDailyProfit null");
                profitDto.setProfitTotal(0);
                profitDto.setProfitTotal(0);
                profitDto.setProfitOfAdvertisements(0);
                profitDto.setVideoNumber(videoDailyProfit.getVideoNumber());
            }
            // 현재 동영상 수익을 반환 목록에 추가
            list.add(profitDto);
        }
        dailyProfitDto.setDailyProfit(list);
        dailyProfitDto.setDate(date);
        return dailyProfitDto;
    }

//    public DailyProfitDto getDailyProfitDto(Video video, LocalDate date) {
//        DailyProfitDto dailyProfitDto = new DailyProfitDto();
//        dailyProfitDto.setDailyProfit(new ArrayList<>());
//        dailyProfitDto.setDate(date);
//        dailyProfitOfVideoRepository.findById(new VideoDailyProfitKey(video.getNumber(), currDate)).ifPresent(videoDailyProfit -> {
//            ProfitDto curr = ProfitDto.builder()
//                    .videoNumber(video.getNumber())
//                    .profitTotal(videoDailyProfit.getDailyProfitOfVideo() + videoDailyProfit.getDailyTotalProfitOfAdvertisements())
//                    .profitOfVideo(videoDailyProfit.getDailyProfitOfVideo())
//                    .profitOfAdvertisements(videoDailyProfit.getDailyTotalProfitOfAdvertisements())
//                    .build();
//            dailyProfitDto.getDailyProfit().add(curr);
//        });
//        return dailyProfitDto
//    }

    public ProfitByPeriodDto showWeeklyProfit(String userEmail, LocalDate date) {
        ProfitByPeriodDto profitByPeriodDto = new ProfitByPeriodDto();
        profitByPeriodDto.setProfitByPeriod(new ArrayList<>());
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        log.info(user.getEmail());
        List<Video> videos = videoRepository.findByUploader(user).orElse(null);
        if (videos == null) {
            return null;
        }
        log.info(videos.size() + "");
        LocalDate monday = getClosestMondayBefore(date);
        profitByPeriodDto.setStartDate(monday);
        profitByPeriodDto.setEndDate(monday.plusDays(6));
        LocalDate currDate = monday;
        for (int i = 1; i <= 7; i ++) {
            if (currDate.isAfter(LocalDate.now())) {
                profitByPeriodDto.setEndDate(LocalDate.now());
                break;
            }
            DailyProfitDto dailyProfitDto = new DailyProfitDto();
            dailyProfitDto.setDailyProfit(new ArrayList<>());
            dailyProfitDto.setDate(currDate);
            for (Video video : videos) {
                dailyProfitOfVideoRepository.findById(new VideoDailyProfitKey(video.getNumber(), currDate)).ifPresent(videoDailyProfit -> {
                    ProfitDto curr = ProfitDto.builder()
                            .videoNumber(video.getNumber())
                            .profitTotal(videoDailyProfit.getDailyProfitOfVideo() + videoDailyProfit.getDailyTotalProfitOfAdvertisements())
                            .profitOfVideo(videoDailyProfit.getDailyProfitOfVideo())
                            .profitOfAdvertisements(videoDailyProfit.getDailyTotalProfitOfAdvertisements())
                            .build();
                    dailyProfitDto.getDailyProfit().add(curr);
                });
            }
            profitByPeriodDto.getProfitByPeriod().add(dailyProfitDto);
            currDate = monday.plusDays(i);
        }
        return profitByPeriodDto;
    }
    public LocalDate getClosestMondayBefore(LocalDate localDate) {
        int fromMon = localDate.getDayOfWeek().compareTo(DayOfWeek.MONDAY);
        return localDate.minusDays(fromMon);
    }

    public LocalDate getFirstDayOfTheMonth(LocalDate localDate) {
        int fromFirstDay = localDate.getDayOfMonth();
        return localDate.minusDays(fromFirstDay-1);
    }

    public LocalDate getFirstDayOfTheYear(LocalDate localDate) {
        int fromFirstDay = localDate.getDayOfYear();
        return localDate.minusDays(fromFirstDay-1);
    }

    public ProfitByPeriodDto showMonthlyProfit(String userEmail, LocalDate date) {
        ProfitByPeriodDto profitByPeriodDto = new ProfitByPeriodDto();
        profitByPeriodDto.setProfitByPeriod(new ArrayList<>());
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        log.info(user.getEmail());
        List<Video> videos = videoRepository.findByUploader(user).orElse(null);
        if (videos == null) {
            return null;
        }
        log.info(videos.size() + "");
        LocalDate firstDayOfTheMonth = getFirstDayOfTheMonth(date);
        profitByPeriodDto.setStartDate(firstDayOfTheMonth);
        int lengthOfTheMonth = date.getMonth().length(firstDayOfTheMonth.isLeapYear());
        profitByPeriodDto.setEndDate(firstDayOfTheMonth.plusDays(lengthOfTheMonth-1));
        LocalDate currDate = firstDayOfTheMonth;
        for (int i = 1; i <= lengthOfTheMonth; i ++) {
            if (currDate.isAfter(LocalDate.now())) {
                profitByPeriodDto.setEndDate(LocalDate.now());
                break;
            }
            DailyProfitDto dailyProfitDto = new DailyProfitDto();
            dailyProfitDto.setDailyProfit(new ArrayList<>());
            dailyProfitDto.setDate(currDate);
            for (Video video : videos) {
                dailyProfitOfVideoRepository.findById(new VideoDailyProfitKey(video.getNumber(), currDate)).ifPresent(videoDailyProfit -> {
                    ProfitDto curr = ProfitDto.builder()
                            .videoNumber(video.getNumber())
                            .profitTotal(videoDailyProfit.getDailyProfitOfVideo() + videoDailyProfit.getDailyTotalProfitOfAdvertisements())
                            .profitOfVideo(videoDailyProfit.getDailyProfitOfVideo())
                            .profitOfAdvertisements(videoDailyProfit.getDailyTotalProfitOfAdvertisements())
                            .build();
                    dailyProfitDto.getDailyProfit().add(curr);
                });
            }
            profitByPeriodDto.getProfitByPeriod().add(dailyProfitDto);
            currDate = firstDayOfTheMonth.plusDays(i);
        }
        return profitByPeriodDto;
    }
    public ProfitByPeriodDto showYearlyProfit(String userEmail, LocalDate date) {
        ProfitByPeriodDto profitByPeriodDto = new ProfitByPeriodDto();
        profitByPeriodDto.setProfitByPeriod(new ArrayList<>());
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        log.info(user.getEmail());
        List<Video> videos = videoRepository.findByUploader(user).orElse(null);
        if (videos == null) {
            return null;
        }
        log.info(videos.size() + "");
        LocalDate firstDayOfTheYear = getFirstDayOfTheYear(date);
        profitByPeriodDto.setStartDate(firstDayOfTheYear);
        int lengthOfTheYear = 365;
        if (date.isLeapYear()) {
            lengthOfTheYear ++;
        }
        profitByPeriodDto.setEndDate(firstDayOfTheYear.plusDays(lengthOfTheYear-1));
        LocalDate currDate = firstDayOfTheYear;
        for (int i = 1; i <= lengthOfTheYear; i ++) {
            if (currDate.isAfter(LocalDate.now())) {
                profitByPeriodDto.setEndDate(LocalDate.now());
                break;
            }
            DailyProfitDto dailyProfitDto = new DailyProfitDto();
            dailyProfitDto.setDailyProfit(new ArrayList<>());
            dailyProfitDto.setDate(currDate);
            for (Video video : videos) {
                dailyProfitOfVideoRepository.findById(new VideoDailyProfitKey(video.getNumber(), currDate)).ifPresent(videoDailyProfit -> {
                    ProfitDto curr = ProfitDto.builder()
                            .videoNumber(video.getNumber())
                            .profitTotal(videoDailyProfit.getDailyProfitOfVideo() + videoDailyProfit.getDailyTotalProfitOfAdvertisements())
                            .profitOfVideo(videoDailyProfit.getDailyProfitOfVideo())
                            .profitOfAdvertisements(videoDailyProfit.getDailyTotalProfitOfAdvertisements())
                            .build();
                    dailyProfitDto.getDailyProfit().add(curr);
                });
            }
            profitByPeriodDto.getProfitByPeriod().add(dailyProfitDto);
            currDate = firstDayOfTheYear.plusDays(i);
        }
        return profitByPeriodDto;
    }

    public Top5ViewsDaily showDailyTop5Views(String userEmail, LocalDate date) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        log.info(user.getEmail());
        List<Video> videos = videoRepository.findByUploader(user).orElse(null);
        if (videos == null) {
            return null;
        }
        Top5ViewsDaily top5ViewsDaily = new Top5ViewsDaily();
        PriorityQueue<ViewsByPeriod> pq = new PriorityQueue<>((o1, o2) -> (int) (o2.getViewsByPeriod() - o1.getViewsByPeriod()));
        for (Video video : videos) {
            dailyProfitOfVideoRepository.findById(new VideoDailyProfitKey(video.getNumber(), date)).ifPresent(videoDailyProfit -> {
                ViewsByPeriod curr = ViewsByPeriod.builder()
                        .videoNumber(videoDailyProfit.getVideoNumber())
                        .viewsByPeriod(videoDailyProfit.getDailyViews())
                        .build();
                pq.add(curr);
            });
        }
        for (int i = 0; i < Math.min(5, videos.size()); i ++) {
            top5ViewsDaily.getViewsByPeriodList().add(pq.poll());
        }
        top5ViewsDaily.setDate(date);
        return top5ViewsDaily;
    }

    public Top5ViewsByPeriod showWeeklyTop5Views(String userEmail, LocalDate date) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        log.info(user.getEmail());
        List<Video> videos = videoRepository.findByUploader(user).orElse(null);
        if (videos == null) {
            return null;
        }
        ViewsByPeriod viewsByPeriod = new ViewsByPeriod();
        PriorityQueue<ViewsByPeriod> pq = new PriorityQueue<>((o1, o2) -> (int) (o2.getViewsByPeriod() - o1.getViewsByPeriod()));
        LocalDate monday = getClosestMondayBefore(date);
        for (Video video : videos) {
            LocalDate currDate = monday;
            long weeklyViews = 0L;
            for (int i = 1; i <= 7; i++) {
                if (currDate.isAfter(LocalDate.now())) {
                    break;
                }
                VideoDailyProfit videoDaily = dailyProfitOfVideoRepository.findById(new VideoDailyProfitKey(video.getNumber(), currDate)).orElse(null);
                if (videoDaily != null) {
                    weeklyViews += videoDaily.getDailyViews();
                }
                currDate = monday.plusDays(i);
            }
            viewsByPeriod.setViewsByPeriod(weeklyViews);
            viewsByPeriod.setVideoNumber(video.getNumber());
            pq.add(viewsByPeriod);
        }
        Top5ViewsByPeriod top5ViewsByPeriod = new Top5ViewsByPeriod();
        top5ViewsByPeriod.setStartDate(monday);
        top5ViewsByPeriod.setEndDate(monday.plusDays(6));
        for (int i = 0; i <= 5; i++) {
            top5ViewsByPeriod.getViewsByPeriodList().add(pq.poll());
        }
        return top5ViewsByPeriod;
    }
//
//        Top5ViewsByPeriod top5ViewsByPeriod = new Top5ViewsByPeriod();
//        LocalDate monday = getClosestMondayBefore(date);
//        top5ViewsByPeriod.setStartDate(monday);
//        top5ViewsByPeriod.setEndDate(monday.plusDays(6));
//        LocalDate currDate = monday;
//        PriorityQueue<ViewsByPeriod> pq = new PriorityQueue<>((o1, o2) -> (int) (o2.getViewsByPeriod() - o1.getViewsByPeriod()));
//        HashMap<Long, ViewsByPeriod> weeklyViews = new HashMap<>();
//        for (int i = 1; i <= 7; i ++) {
//            if (currDate.isAfter(LocalDate.now())) {
//                top5ViewsByPeriod.setEndDate(LocalDate.now());
//                break;
//            }
//            for (Video video : videos) {
//                dailyProfitOfVideoRepository.findById(new VideoDailyProfitKey(video.getNumber(), date)).ifPresent(videoDailyProfit -> {
//                    weeklyViews.put(video.getNumber(), weeklyViews.getOrDefault(video.getNumber(), new ViewsByPeriod(video.getNumber(), videoDailyProfit.getDailyViews()+));
//                });
//            }
//        }
//    }
}
