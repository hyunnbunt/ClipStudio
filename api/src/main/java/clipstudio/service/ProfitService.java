package clipstudio.service;

import clipstudio.entity.Video;
import clipstudio.entity.profit.TotalProfit;
import clipstudio.entity.profit.TotalProfitKey;
import clipstudio.dto.profit.ProfitDto;
import clipstudio.dto.profit.ProfitDetail;
import clipstudio.dto.profit.ProfitByPeriodDto;
import clipstudio.dto.stastistics.Top5ViewsByPeriod;
import clipstudio.dto.stastistics.Top5Views;
import clipstudio.entity.User;
import clipstudio.repository.UserRepository;
import clipstudio.repository.batch.TotalProfitRepository;
import clipstudio.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfitService {
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final TotalProfitRepository totalProfitRepository;

    public ProfitByPeriodDto showProfit(String userEmail, LocalDate date) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
//        List<Video> videos = videoRepository.findByUploader(user).orElseThrow();
//        ProfitDto profitDto = new ProfitDto(date);
//        for (Video video : videos) {
//            TotalProfit profit = totalProfitRepository.findById(new TotalProfitKey(video.getNumber(), date)).orElseThrow();
//            profitDto.addProfit(ProfitDetail.fromEntity(profit));
//        }
//        return profitDto;
        return showProfitByPeriod(user, date, date);
    }

    public ProfitByPeriodDto showProfitByPeriod(User user, LocalDate start, LocalDate end) {
//        List<TotalProfit> weeklyProfitList = totalProfitRepository.findAllByUploaderNumberAndDateBetween(user.getNumber(), start, end);

        List<TotalProfit> weeklyProfitList = totalProfitRepository.findAllByDateBetween(start, end);
        ProfitByPeriodDto profitByPeriodDto = new ProfitByPeriodDto(start, end);
        for (TotalProfit videoProfit : weeklyProfitList) {
            profitByPeriodDto.add(videoProfit);
        }
        return profitByPeriodDto;
    }

    public ProfitByPeriodDto showWeeklyProfit(String userEmail, LocalDate date) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        LocalDate start = getClosestMondayBefore(date);
        LocalDate end = start.plusDays(6L);
        return showProfitByPeriod(user, start, end);
    }

    public ProfitByPeriodDto showMonthlyProfit(String userEmail, LocalDate date) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        LocalDate start = getFirstDayOfTheMonth(date);
        int daysOfMonth = date.getMonth().length(date.isLeapYear());
        LocalDate end = start.plusDays(daysOfMonth-1);
        return showProfitByPeriod(user, start, end);
    }

    public ProfitByPeriodDto showYearlyProfit(String userEmail, LocalDate date) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        LocalDate start = getFirstDayOfTheYear(date);
        int daysOfYear = 365;
        if (date.isLeapYear()) {
            daysOfYear = 366;
        }
        LocalDate end = start.plusDays(daysOfYear-1);
        return showProfitByPeriod(user, start, end);
    }

    public Top5Views showTop5Views(String userEmail, LocalDate date) {
        String userEmail2 = "yzt@fmdms.com";
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        List<TotalProfit> histories  = totalProfitRepository.findAllByUploaderNumberAndDateBetween(user.getNumber(), date, date);
        PriorityQueue<TotalProfit> pq = new PriorityQueue<>((o1, o2) -> (int) (o2.getViews() - o1.getViews()));
        pq.addAll(histories);
        Top5Views top5Views = new Top5Views(date);
        for (int i = 0; i < Math.min(5, histories.size()); i ++) {
            top5Views.addToTop5(pq.poll());
        }
        return top5Views;
    }


    public Top5ViewsByPeriod showTop5ViewsByPeriod(User user, LocalDate start, LocalDate end) {
        Map<Long, Long> views = new HashMap<>();
        long startTest = System.currentTimeMillis();
        List<TotalProfit> histories  = totalProfitRepository.findAllByUploaderNumberAndDateBetween(user.getNumber(), start, end);
        long executionTime = System.currentTimeMillis() - startTest;
        log.info(executionTime+"");
        for (TotalProfit history : histories) {
            Long videoNumber = history.getVideoNumber();
            views.put(history.getVideoNumber(), views.getOrDefault(videoNumber, 0L) + history.getViews());
        }
        Map<Long, Long> sortedByViews = sortByValue(views);
        Top5ViewsByPeriod top5ViewsByPeriod = new Top5ViewsByPeriod(start, end);
        Iterator<Map.Entry<Long, Long>> iter = sortedByViews.entrySet().iterator();
        for (int i = 0; i < 5; i ++) {
            if (!iter.hasNext()) {
                break;
            }
            Map.Entry<Long, Long> entry = iter.next();
            top5ViewsByPeriod.update(entry.getKey(), entry.getValue());
        }
        return top5ViewsByPeriod;
    }

    public Top5ViewsByPeriod showWeeklyTop5Views(String userEmail, LocalDate date) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        LocalDate start = getClosestMondayBefore(date);
        LocalDate end = start.plusDays(6L);
        return showTop5ViewsByPeriod(user, start, end);
    }

    public Top5ViewsByPeriod showMonthlyTop5Views(String userEmail, LocalDate date) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        LocalDate start = getFirstDayOfTheMonth(date);
        int daysOfMonth = date.getMonth().length(date.isLeapYear());
        LocalDate end = start.plusDays(daysOfMonth-1);
        return showTop5ViewsByPeriod(user, start, end);
    }

    public Top5ViewsByPeriod showYearlyTop5Views(String userEmail, LocalDate date) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        LocalDate start = getFirstDayOfTheYear(date);
        int daysOfYear = 365;
        if (date.isLeapYear()) {
            daysOfYear = 366;
        }
        LocalDate end = start.plusDays(daysOfYear-1);
        return showTop5ViewsByPeriod(user, start, end);
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
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


//    public ProfitByPeriodDto showWeeklyProfit2(String userEmail, LocalDate date) {
//        ProfitByPeriodDto profitByPeriodDto = new ProfitByPeriodDto();
//        profitByPeriodDto.setProfitByPeriod(new ArrayList<>());
//        User user = userRepository.findByEmail(userEmail).orElseThrow();
//        log.info(user.getEmail());
//        List<Video> videos = videoRepository.findByUploader(user).orElse(null);
//        if (videos == null) {
//            return null;
//        }
//        log.info(videos.size() + "");
//        LocalDate monday = getClosestMondayBefore(date);
//        profitByPeriodDto.setStartDate(monday);
//        profitByPeriodDto.setEndDate(monday.plusDays(6));
//        LocalDate currDate = monday;
//        for (int i = 1; i <= 7; i ++) {
//            if (currDate.isAfter(LocalDate.now())) {
//                profitByPeriodDto.setEndDate(LocalDate.now());
//                break;
//            }
//            DailyProfitDto dailyProfitDto = new DailyProfitDto();
//            dailyProfitDto.setDailyProfit(new ArrayList<>());
//            dailyProfitDto.setDate(currDate);
//            for (Video video : videos) {
//                dailyProfitOfVideoRepository.findById(new VideoDailyProfitKey(video.getNumber(), currDate)).ifPresent(videoDailyProfit -> {
//                    ProfitDto curr = ProfitDto.builder()
//                            .videoNumber(video.getNumber())
//                            .profitTotal(videoDailyProfit.getDailyProfitOfVideo() + videoDailyProfit.getDailyTotalProfitOfAdvertisements())
//                            .profitOfVideo(videoDailyProfit.getDailyProfitOfVideo())
//                            .profitOfAdvertisements(videoDailyProfit.getDailyTotalProfitOfAdvertisements())
//                            .build();
//                    dailyProfitDto.getDailyProfit().add(curr);
//                });
//            }
//            profitByPeriodDto.getProfitByPeriod().add(dailyProfitDto);
//            currDate = monday.plusDays(i);
//        }
//    }

//    public Top5ViewsDaily showTop5Views(String userEmail, LocalDate date) {
//        User user = userRepository.findByEmail(userEmail).orElseThrow();
//        Top5ViewsDaily top5ViewsDaily = new Top5ViewsDaily();
//        PriorityQueue<ViewsByPeriod> pq = new PriorityQueue<>((o1, o2) -> (int) (o2.getViewsByPeriod() - o1.getViewsByPeriod()));
//        List<Video> videos = videoRepository.findByUploader(user).orElseThrow();
//        for (Video video : videos) {
//            profitRepository.findById(new VideoDailyProfitKey(video.getNumber(), date)).ifPresent(videoDailyProfit -> {
//                ViewsByPeriod curr = ViewsByPeriod.builder()
//                        .videoNumber(videoDailyProfit.getVideoNumber())
//                        .viewsByPeriod(videoDailyProfit.getDailyViews())
//                        .build();
//                pq.add(curr);
//            });
//        }
//        for (int i = 0; i < Math.min(5, videos.size()); i ++) {
//            top5ViewsDaily.getViewsByPeriodList().add(pq.poll());
//        }
//        top5ViewsDaily.setDate(date);
//        return top5ViewsDaily;
//    }

    /**
     public Top5ViewsByPeriod showWeeklyTop5Views2(String userEmail, LocalDate date) {
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
     VideoDailyProfit videoDaily = profitRepository.findById(new VideoDailyProfitKey(video.getNumber(), currDate)).orElse(null);
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
     } */
}
