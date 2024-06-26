package clipstudio.config;

import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
@Slf4j
public class BatchScheduler {
    private final JobLauncher jobLauncher;
    private final Job profitCalculationJob;
    private static String testDate = "2013-01-01";
    private ThreadPoolTaskScheduler taskScheduler;

    @PostConstruct
    public void init() {
        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.initialize();
    }
    @Scheduled(fixedRate = 200)  // 5000, 5초마다 실행
    public void runJob() throws Exception {

        JobExecution jobExecution = jobLauncher.run(profitCalculationJob, new JobParametersBuilder()
                .addString("batchDate", testDate, true)
                .toJobParameters());
        log.info(jobExecution.getJobParameters().toString());
        LocalDate curr = LocalDate.parse(testDate).plusDays(1);
        if (curr.equals(LocalDate.parse("2013-12-31"))) {
            for (int i = 0; i < 100; i ++) {
                log.info("end");
            }
            taskScheduler.initiateShutdown();
        } else {
            testDate = curr.toString();
        }
    }
}
