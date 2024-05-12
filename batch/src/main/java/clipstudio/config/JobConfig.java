package clipstudio.config;

import clipstudio.dto.Advertisement;
import clipstudio.dto.AdvertisementDailyHistory;
import clipstudio.dto.Video;
import clipstudio.dto.VideoDailyHistory;
import clipstudio.mapper.AdvertisementMapper;
import clipstudio.mapper.VideoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class JobConfig {
    /**
     * Note the JobRepository is typically autowired in and not needed to be explicitly
     * configured
     */

    private final ItemProcessor<Video, VideoDailyHistory> videoProfitCalculationProcessor;
    private final ItemProcessor<Advertisement, AdvertisementDailyHistory> advertisementProfitCalculationProcessor;
    private final Step videoProfitCalculationStep;
    private final Step advertisementProfitCalculationStep;
    private final DataSource dataSource;
    private final AdvertisementMapper advertisementMapper;
    private final VideoMapper videoMapper;

    @Bean
    public JdbcTransactionManager transactionManager() {
        return new JdbcTransactionManager(dataSource);
    }
    @Bean
    public Job dailyProfitCalculationJob(JobRepository jobRepository) throws ParseException{
        return new JobBuilder("dailyProfitCalculationJob", jobRepository)
                .incrementer(new RunIdIncrementer()) // test environment, 중복 실행 허용
                .start(videoProfitCalculationStep)
                .next(advertisementProfitCalculationStep)
                .build();
    }

    @Bean
    @JobScope
    public Step advertisementProfitCalculationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                                   @Value("#{jobParameters[batchDate]}") String batchDate) throws ParseException {
        log.info(String.valueOf(new SimpleDateFormat("yyyy-MM-dd").parse(batchDate)));
        return new StepBuilder("advertisementProfitCalculationStep", jobRepository)
                .<Advertisement, AdvertisementDailyHistory>chunk(10, transactionManager)
                .reader(advertisementReader())
                .processor(advertisementProfitCalculationProcessor)
                .writer(advertisementCompositeWriter())
                .build();
    }

    @Bean
    @JobScope // 빈의 생성 시점을 지정된 Scope가 실행되는 시점으로 지연 (late binding), 동일한 컴포넌트를 병렬 처리할 때 유리
    public Step videoProfitCalculationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                           @Value("#{jobParameters[batchDate]}") String batchDate) throws ParseException {
        log.info(String.valueOf(new SimpleDateFormat("yyyy-MM-dd").parse(batchDate)));
        return new StepBuilder("videoProfitCalculationStep", jobRepository)
                .<Video, VideoDailyHistory>chunk(10, transactionManager) // test if the result changes by chunk size.
                //	transactionManager: Spring’s PlatformTransactionManager that begins
                //	and commits transactions during processing.
                .reader(videoReader())
//                .allowStartIfComplete(true) // test environment, 중복 실행 허용
                .processor(videoProfitCalculationProcessor)
//                .writer(videoProfitWriter())
                .writer(videoCompositeWriter())
                .build();
    }


    @Bean
    public JdbcCursorItemReader<Advertisement> advertisementReader() {
        JdbcCursorItemReader<Advertisement> reader = new JdbcCursorItemReaderBuilder<Advertisement>()
                .name("advertisementReader")
                .dataSource(dataSource)
                .rowMapper(advertisementMapper)
                .sql("SELECT * FROM advertisements")
                .build();
        return reader;
    }



    @Bean
    public JdbcCursorItemReader<Video> videoReader() {
        JdbcCursorItemReader<Video> reader = new JdbcCursorItemReaderBuilder<Video>()
                .name("videoReader")
                .dataSource(dataSource)
                .rowMapper(videoMapper)
                .sql("SELECT * FROM videos")
                .build();
        return reader;
    }//  batchDate=2024-05-10

    @Bean
    public CompositeItemWriter advertisementCompositeWriter() {
        List<JdbcBatchItemWriter> writers = List.of(initializeAdvertisementTempDailyViewsWriter(), advertisementProfitWriter());
        return new CompositeItemWriterBuilder()
                .delegates(writers).build();
    }
    @Bean
    public CompositeItemWriter videoCompositeWriter() {
        List<JdbcBatchItemWriter> writers = List.of(initializeVideoTempDailyViewsWriter(), videoProfitWriter());
        return new CompositeItemWriterBuilder()
                .delegates(writers).build();
    }

    @Bean
    public JdbcBatchItemWriter<AdvertisementDailyHistory> initializeAdvertisementTempDailyViewsWriter() {
        return new JdbcBatchItemWriterBuilder<AdvertisementDailyHistory>()
                .sql("UPDATE advertisements SET temp_daily_views=0, total_views=:updatedTotalViews WHERE number=:advertisementNumber")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }
    @Bean
    public JdbcBatchItemWriter<VideoDailyHistory> initializeVideoTempDailyViewsWriter() {

        return new JdbcBatchItemWriterBuilder<VideoDailyHistory>()
                .sql("UPDATE videos SET temp_daily_views=0, total_views=:updatedTotalViews where number=:videoNumber")
                .dataSource(dataSource)
                .beanMapped() // ?? https://jojoldu.tistory.com/339
                .build();
    }
    @Bean
    public JdbcBatchItemWriter<AdvertisementDailyHistory> advertisementProfitWriter() {
        return new JdbcBatchItemWriterBuilder<AdvertisementDailyHistory>()
                .sql("INSERT INTO advertisement_daily_histories(advertisement_number, calculated_date, daily_views, daily_profit) VALUES(:advertisementNumber, :calculatedDate, :dailyViews, :dailyProfit)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }
    @Bean
    public JdbcBatchItemWriter<VideoDailyHistory> videoProfitWriter() {
        /**
         * The JdbcBatchItemWriter is an ItemWriter that uses the batching features
         * from NamedParameterJdbcTemplate to execute a batch of statements
         * for all items provided. Spring Batch provides a JdbcBatchItemWriterBuilder
         * to construct an instance of the JdbcBatchItemWriter.
        */

        return new JdbcBatchItemWriterBuilder<VideoDailyHistory>()
                .sql("INSERT INTO video_daily_histories(video_number, calculated_date, daily_views, daily_profit) VALUES(:videoNumber, :calculatedDate, :dailyViews, :dailyProfit)")
                .dataSource(dataSource)
                .beanMapped() // ?? https://jojoldu.tistory.com/339
                .build();
    } // JpaItemWriter?

}

