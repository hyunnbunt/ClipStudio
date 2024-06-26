package clipstudio.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;


@RequiredArgsConstructor
@Configuration
@Slf4j
public class JobConfig {
    /**
     * Note the JobRepository is typically autowired in and not needed to be explicitly
     * configured
     */

    // Video step
    private final Step videoProfitStep;
    // Advertisement step
    private final Step advertisementProfitStep;
    private final DataSource dataSource;

    @Bean
    public JdbcTransactionManager transactionManager() {
        return new JdbcTransactionManager(dataSource);
    }

    @Bean
    public Job profitCalculationJob(JobRepository jobRepository) {
        return new JobBuilder("profitCalculationJob", jobRepository)
                .incrementer(new RunIdIncrementer()) // test environment, 중복 실행 허용
                .start(advertisementProfitStep) // test 상황에서 임시로 코멘트 처리
                .next(videoProfitStep)
                .build();
    }

}

