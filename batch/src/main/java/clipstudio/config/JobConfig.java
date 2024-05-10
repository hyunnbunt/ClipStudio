package clipstudio.config;

import clipstudio.dto.DailyViews;
import clipstudio.dto.VideoViewPrice;
import clipstudio.mapper.VideoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;
import clipstudio.dto.DailyProfitOfVideo;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class JobConfig {
    /**
     * Note the JobRepository is typically autowired in and not needed to be explicitly
     * configured
     */

    private final ItemProcessor<DailyViews, DailyProfitOfVideo> videoProfitCalculationProcessor;
    private final DataSource dataSource;
    private final VideoMapper videoMapper;

    @Bean
    public JdbcTransactionManager transactionManager() {
        return new JdbcTransactionManager(dataSource);
    }
    @Bean
    public Job dailyProfitCalculationJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws ParseException{
        return new JobBuilder("dailyProfitCalculationJob", jobRepository)
                .incrementer(new RunIdIncrementer()) // test environment, 중복 실행 허용
                .start(dailyProfitCalculationStep(jobRepository, transactionManager, null))
                .build();
    }

    @Bean
    @JobScope // 빈의 생성 시점을 지정된 Scope가 실행되는 시점으로 지연 (late binding), 동일한 컴포넌트를 병렬 처리할 때 유리
    public Step dailyProfitCalculationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                           @Value("#{jobParameters[batchDate]}") String batchDate) throws ParseException {
        log.info("when does incrementer increment id?: ");
        log.info(String.valueOf(new SimpleDateFormat("yyyy-MM-dd").parse(batchDate)));
        return new StepBuilder("dailyProfitCalculationStep", jobRepository)
                .<DailyViews, DailyProfitOfVideo>chunk(1, transactionManager)
                //	transactionManager: Spring’s PlatformTransactionManager that begins
                //	and commits transactions during processing.
                .reader(videoReader())
//                .allowStartIfComplete(true) // test environment, 중복 실행 허용
                .processor(videoProfitCalculationProcessor)
                .writer(videoProfitWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<DailyViews> videoReader() {
        JdbcCursorItemReader<DailyViews> reader = new JdbcCursorItemReaderBuilder<DailyViews>()
                .name("videoReader")
                .dataSource(dataSource)
                .rowMapper(videoMapper)
                .sql("SELECT * FROM videos WHERE number=10")
                .build();
        return reader;
    }//  batchDate=2024-05-10


    @Bean
    public JdbcBatchItemWriter<DailyProfitOfVideo> videoProfitWriter() {
        /**
         * The JdbcBatchItemWriter is an ItemWriter that uses the batching features
         * from NamedParameterJdbcTemplate to execute a batch of statements
         * for all items provided. Spring Batch provides a JdbcBatchItemWriterBuilder
         * to construct an instance of the JdbcBatchItemWriter.
        */

        return new JdbcBatchItemWriterBuilder<DailyProfitOfVideo>()
                .sql("INSERT INTO daily_profit_of_video(video_number, calculated_date, daily_profit) VALUES(:videoNumber, current_date(), :dailyProfit)")
                .dataSource(dataSource)
                .beanMapped() // ?? https://jojoldu.tistory.com/339
                .build();
    } // JpaItemWriter?
}

