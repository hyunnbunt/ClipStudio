package clipstudio.config;

import clipstudio.dto.DailyViews;
import clipstudio.mapper.VideoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;
import clipstudio.Entity.DailyProfitOfVideo;
@EnableBatchProcessing
@RequiredArgsConstructor
@Configuration
@Slf4j
public class JobConfig {
    /**
     * Note the JobRepository is typically autowired in and not needed to be explicitly
     * configured
     */

    private final ItemProcessor<DailyViews, DailyProfitOfVideo> videoProfitCalculationProcessor;
    private final DatabaseConfig databaseConfig;
    private final VideoMapper videoMapper;

    @Bean
    public JdbcTransactionManager transactionManager() {
        return new JdbcTransactionManager(databaseConfig.getDataSource());
    }
    @Bean
    public Job dailyProfitCalculationJob(JobRepository jobRepository) {
        return new JobBuilder("dailyProfitCalculationJob", jobRepository)
                .preventRestart() // why?
                .start(dailyProfitCalculationStep(jobRepository))
                .build();
    }

    @Bean
    public Step dailyProfitCalculationStep(JobRepository jobRepository) {
        return new StepBuilder("dailyProfitCalculationStep", jobRepository)
                .<DailyViews, DailyProfitOfVideo>chunk(1, transactionManager())
                //	transactionManager: Spring’s PlatformTransactionManager that begins
                //	and commits transactions during processing.
                .reader(videoReader())
                .processor(videoProfitCalculationProcessor)
                .writer(videoProfitWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<DailyViews> videoReader() {
        JdbcCursorItemReader<DailyViews> reader = new JdbcCursorItemReaderBuilder<DailyViews>()
                .name("videoReader")
                .dataSource(databaseConfig.getDataSource())
                .rowMapper(videoMapper)
                .sql("SELECT * FROM videos")
                .build();
        return reader;
    }


    @Bean
    public JdbcBatchItemWriter<DailyProfitOfVideo> videoProfitWriter() {
        /**
         * The JdbcBatchItemWriter is an ItemWriter that uses the batching features
         * from NamedParameterJdbcTemplate to execute a batch of statements
         * for all items provided. Spring Batch provides a JdbcBatchItemWriterBuilder
         * to construct an instance of the JdbcBatchItemWriter.
        */

        return new JdbcBatchItemWriterBuilder<DailyProfitOfVideo>()
                .sql("INSERT INTO daily_profit_of_video(calculated_date, daily_profit) VALUES(current_date(), :dailyProfit)")
                .dataSource(databaseConfig.getDataSource())
                .beanMapped() // ?? https://jojoldu.tistory.com/339
                .build();
    } // JpaItemWriter?
}

