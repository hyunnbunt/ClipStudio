package clipstudio.config;

import clipstudio.dto.DailyViews;
import clipstudio.mapper.DailyViewsRowMapper;
import com.zaxxer.hikari.HikariDataSource;
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
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.support.JdbcTransactionManager;
import clipstudio.Entity.DailyProfitOfVideo;
@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
public class JobConfiguration {
    /**
     * Note the JobRepository is typically autowired in and not needed to be explicitly
     * configured
     */

    private final ItemProcessor<DailyViews, DailyProfitOfVideo> videoProfitCalculationProcessor;
    @Bean
    public HikariDataSource dataSource() {
            return DataSourceBuilder.create()
                    .url("jdbc:mariadb://127.0.0.1:3308/clipstudio")
                    .username("clipst")
                    .password("1234")
                    .driverClassName("org.mariadb.jdbc.Driver")
                    .type(HikariDataSource.class)
                    .build();
    }

    @Bean
    public JdbcTransactionManager transactionManager() {
        return new JdbcTransactionManager(dataSource());
    }

    @Bean
    public Job dailyProfitCalculationJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("dailyProfitCalculationJob", jobRepository)
                .preventRestart() // why?
                .start(dailyProfitCalculationStep(jobRepository))
                .build();
    }

    @Bean
    public Step dailyProfitCalculationStep(JobRepository jobRepository) {
        return new StepBuilder("dailyProfitCalculationStep", jobRepository)
                .<DailyViews, DailyProfitOfVideo>chunk(20, transactionManager())
                //	transactionManager: Spring’s PlatformTransactionManager that begins
                //	and commits transactions during processing.
                .reader(getDailyViewsReader())
                .processor(videoProfitCalculationProcessor)
                .writer(getDailyProfitOfVideoWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<DailyViews> getDailyViewsReader() {
        return new JdbcCursorItemReaderBuilder<DailyViews>()
                .name("dailyViewsReader")
                .rowMapper(new DailyViewsRowMapper())
                .sql("SELECT * FROM videos")
                .build();
    }


    @Bean
    public JdbcBatchItemWriter<DailyProfitOfVideo> getDailyProfitOfVideoWriter() {
        /**
         * The JdbcBatchItemWriter is an ItemWriter that uses the batching features
         * from NamedParameterJdbcTemplate to execute a batch of statements
         * for all items provided. Spring Batch provides a JdbcBatchItemWriterBuilder
         * to construct an instance of the JdbcBatchItemWriter.
        */
        return new JdbcBatchItemWriterBuilder<DailyProfitOfVideo>()
                .sql("INSERT INTO daily_profit_of_video(calculated_date, daily_profit) VALUES(current_date(), :dailyProfit)")
                .beanMapped() // ?? https://jojoldu.tistory.com/339
                .build();
    } // JpaItemWriter
}

