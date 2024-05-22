package clipstudio.config;

import clipstudio.ReadListener;
import clipstudio.dto.AdvertisementDto;
import clipstudio.dto.VideoDto;
import clipstudio.mapper.AdvertisementMapper;
import clipstudio.mapper.VideoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.net.http.WebSocket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;


@RequiredArgsConstructor
@Configuration
@Slf4j
public class JobConfig {
    /**
     * Note the JobRepository is typically autowired in and not needed to be explicitly
     * configured
     */

    private final ItemProcessor<VideoDto, VideoDto> videoProfitCalculationProcessor;
    private final ItemProcessor<AdvertisementDto, AdvertisementDto> advertisementProfitCalculationProcessor;
    private final Step videoDailyProfitStep;
    private final Step advertisementDailyProfitStep;
    private final DataSource dataSource;
    private final AdvertisementMapper advertisementMapper;
    private final VideoMapper videoMapper;
    private final ExecutorServiceConfig executorServiceConfig;

    @Bean
    public JdbcTransactionManager transactionManager() {
        return new JdbcTransactionManager(dataSource);
    }
    @Bean
    @Scheduled(cron = "0 58 22 * * *")
    public Job dailyProfitJob(JobRepository jobRepository) throws ParseException{
        return new JobBuilder("dailyProfitJob", jobRepository)
                .incrementer(new RunIdIncrementer()) // test environment, 중복 실행 허용
                .start(advertisementDailyProfitStep)
                .next(videoDailyProfitStep)
                .build();
    }

    @Bean
    @JobScope
    public Step advertisementDailyProfitStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                        @Value("#{jobParameters['batchDate']}") String batchDate) throws ParseException {
        log.info(String.valueOf(new SimpleDateFormat("yyyy-MM-dd").parse(batchDate)));
        return new StepBuilder("advertisementDailyProfitStep", jobRepository)
                .<AdvertisementDto, AdvertisementDto>chunk(10, transactionManager)
                .reader(syncAdvertisementReader())
                .processor(advertisementProfitCalculationProcessor)
                .writer(advertisementCompositeWriter())
                .taskExecutor(
                    new ConcurrentTaskExecutor(
                            executorServiceConfig.virtualThreadExecutor() //가상 스레드
                    ))
//                .taskExecutor(executorServiceConfig.executor())
                .build();
    }

    @Bean
    @JobScope // 빈의 생성 시점을 지정된 Scope가 실행되는 시점으로 지연 (late binding), 동일한 컴포넌트를 병렬 처리할 때 유리
    public Step videoDailyProfitStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                     @Value("#{jobParameters[batchDate]}") String batchDate) throws ParseException {
        log.info(String.valueOf(new SimpleDateFormat("yyyy-MM-dd").parse(batchDate)));
        return new StepBuilder("videoDailyProfitStep", jobRepository)
                .<VideoDto, VideoDto>chunk(10, transactionManager) // test if the result changes by chunk size.
                //	transactionManager: Spring’s PlatformTransactionManager that begins and commits transactions during processing.
                .reader(syncVideoReader())
                .listener(new ReadListener()) // (1)
//                .allowStartIfComplete(true) // test environment, 중복 실행 허용
                .processor(videoProfitCalculationProcessor)
                .writer(videoCompositeWriter())
                .taskExecutor(
                        new ConcurrentTaskExecutor(
                                executorServiceConfig.virtualThreadExecutor() //가상 스레드
                        ))
//                .taskExecutor(executorServiceConfig.executor())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<AdvertisementDto> advertisementReader() {
        JdbcCursorItemReader<AdvertisementDto> reader = new JdbcCursorItemReaderBuilder<AdvertisementDto>()
                .name("advertisementReader")
                .dataSource(dataSource)
                .rowMapper(advertisementMapper)
                .sql("SELECT * FROM advertisements")
                .build();
        return reader;
    }

    @Bean
    public JdbcCursorItemReader<VideoDto> videoReader() {
        JdbcCursorItemReader<VideoDto> reader = new JdbcCursorItemReaderBuilder<VideoDto>()
                .name("videoReader")
                .dataSource(dataSource)
                .rowMapper(videoMapper)
                .sql("SELECT * FROM videos")
                .build();
        return reader;
    }//  batchDate=2024-05-10

    @Bean
    public SynchronizedItemStreamReader<AdvertisementDto> syncAdvertisementReader() {
        JdbcCursorItemReader<AdvertisementDto> reader = new JdbcCursorItemReaderBuilder<AdvertisementDto>()
                .name("advertisementReader")
                .dataSource(dataSource)
                .rowMapper(advertisementMapper)
                .sql("SELECT * FROM advertisements")
                .build();
        return new SynchronizedItemStreamReaderBuilder<AdvertisementDto>()
                .delegate(reader)
                .build();
    }//  batchDate=2024-05-10
    @Bean
    public SynchronizedItemStreamReader<VideoDto> syncVideoReader() {
        JdbcCursorItemReader<VideoDto> reader = new JdbcCursorItemReaderBuilder<VideoDto>()
                .name("videoReader")
                .dataSource(dataSource)
                .rowMapper(videoMapper)
                .sql("SELECT * FROM videos")
                .build();
        return new SynchronizedItemStreamReaderBuilder<VideoDto>()
                .delegate(reader)
                .build();
    }//  batchDate=2024-05-10

    // The @ConditionalOnThreading annotation allows the creation of a bean only if Spring is configured to use a specific type of threading internally. By type of threads, it means either the platform threads or virtual threads. To recall, since Java 21, we have had the ability to use virtual threads instead of platform ones.
    //
    //So, to configure Spring to use virtual threads internally, we use a property named spring.threads.virtual.enabled. If this property is true and we’re running on Java 21 or higher, then @ConditionalOnThreading annotation would allow the creation of the bean.
    // 광고 정산 writer
    @Bean
    public CompositeItemWriter advertisementCompositeWriter() {
        List<JdbcBatchItemWriter> writers = List.of(initializeAdvertisementTempDailyViewsWriter(), updateAdvertisementDailyProfitWriter());
        return new CompositeItemWriterBuilder()
                .delegates(writers).build();
    }

    @Bean
    public JdbcBatchItemWriter<AdvertisementDto> initializeAdvertisementTempDailyViewsWriter() {
        return new JdbcBatchItemWriterBuilder<AdvertisementDto>()
                .sql("UPDATE advertisements SET temp_daily_views=0, total_views=:totalViews WHERE number=:number")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<AdvertisementDto> updateAdvertisementDailyProfitWriter() {
        return new JdbcBatchItemWriterBuilder<AdvertisementDto>()
                .sql("INSERT INTO advertisement_daily_profit(advertisement_number, calculated_date, daily_views, daily_profit) VALUES(:number, :calculatedDate, :dailyViews, :dailyProfit)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    // 동영상 정산 writer
    @Bean
    public CompositeItemWriter videoCompositeWriter() {
        List<JdbcBatchItemWriter> writers = List.of(initializeVideoTempDailyViewsWriter(), updateVideoDailyProfitWriter());
        return new CompositeItemWriterBuilder()
                .delegates(writers).build();
    }


    @Bean
    public JdbcBatchItemWriter<VideoDto> initializeVideoTempDailyViewsWriter() {
        return new JdbcBatchItemWriterBuilder<VideoDto>()
                .sql("UPDATE videos SET temp_daily_views=0, total_views=:totalViews where number=:number")
                .dataSource(dataSource)
                .beanMapped() // ?? https://jojoldu.tistory.com/339
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<VideoDto> updateVideoDailyProfitWriter() {
        /**
         * The JdbcBatchItemWriter is an ItemWriter that uses the batching features
         * from NamedParameterJdbcTemplate to execute a batch of statements
         * for all items provided. Spring Batch provides a JdbcBatchItemWriterBuilder
         * to construct an instance of the JdbcBatchItemWriter.
        */

        return new JdbcBatchItemWriterBuilder<VideoDto>()
                .sql("INSERT INTO video_daily_profit(video_number, calculated_date, daily_views, daily_profit_of_video, daily_total_profit_of_advertisements) VALUES(:number, :calculatedDate, :dailyViews, :dailyProfitOfVideo, :dailyTotalProfitOfAdvertisements)")
                .dataSource(dataSource)
                .beanMapped() // ?? https://jojoldu.tistory.com/339
                .build();
    } // JpaItemWriter?

}

