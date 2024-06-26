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
    private ThreadPoolTaskScheduler taskScheduler;

    @PostConstruct
    public void init() {
        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.initialize();
    }

    @Scheduled(cron = "0 58 22 * * *")
    public void runJob() throws Exception {
        JobExecution jobExecution = jobLauncher.run(profitCalculationJob, new JobParametersBuilder()
                .addString("batchDate", LocalDate.now().toString(), true)
                .toJobParameters());
    }
}
