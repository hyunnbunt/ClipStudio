package clipstudio.config;

import clipstudio.dto.AdvertisementDto;
import clipstudio.mapper.AdvertisementMapper;
import clipstudio.mapper.VideoMapper;
import clipstudio.processor.AdvertisementProfitProcessor;
import clipstudio.processor.VideoProfitProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.Executor;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AdvertisementStepConfig {

    private final DataSource dataSource;
    private final AdvertisementMapper advertisementMapper;
    private final AdvertisementProfitProcessor advertisementProfitProcessor;
    private final ExecutorServiceConfig executorServiceConfig;
    private final Executor virtualThreadExecutor;

    @Bean
    @JobScope
    public Step advertisementDailyProfitStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                             @Value("#{jobParameters['batchDate']}") String batchDate) throws ParseException {
        log.info(String.valueOf(new SimpleDateFormat("yyyy-MM-dd").parse(batchDate)));
        return new StepBuilder("advertisementDailyProfitStep", jobRepository)
                .<AdvertisementDto, AdvertisementDto>chunk(20, transactionManager)
                .reader(syncAdvertisementReader())
                .processor(advertisementProfitProcessor)
                .writer(advertisementCompositeWriter())
                .taskExecutor(
                    new ConcurrentTaskExecutor(
                            executorServiceConfig.virtualThreadExecutor() //가상 스레드
                    ))
                .taskExecutor(executorServiceConfig.executor()) // 멀티 스레드
                .build();
    }

    @Bean
    public JdbcCursorItemReader<AdvertisementDto> advertisementReader() { // single-thread 환경에서 사용
        JdbcCursorItemReader<AdvertisementDto> reader = new JdbcCursorItemReaderBuilder<AdvertisementDto>()
                .name("advertisementReader")
                .dataSource(dataSource)
                .rowMapper(advertisementMapper)
                .sql("SELECT * FROM advertisements")
                .build();
        return reader;
    }

    @Bean
    @StepScope
    public SynchronizedItemStreamReader<AdvertisementDto> syncAdvertisementReader() { // multi-thread 환경에서 사용
        JdbcCursorItemReader<AdvertisementDto> reader = new JdbcCursorItemReaderBuilder<AdvertisementDto>()
                .name("advertisementReader")
                .dataSource(dataSource)
                .rowMapper(advertisementMapper)
                .sql("SELECT * FROM advertisements")
                .build();
        return new SynchronizedItemStreamReaderBuilder<AdvertisementDto>()
                .delegate(reader)
                .build();
    }


    @Bean
    @StepScope
    public JdbcBatchItemWriter<AdvertisementDto> initializeAdvertisementTempDailyViewsWriter() {
        return new JdbcBatchItemWriterBuilder<AdvertisementDto>()
                .sql("UPDATE advertisements SET today_views=0, total_views=:totalViews WHERE number=:number")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<AdvertisementDto> updateAdvertisementDailyProfitWriter() {
        return new JdbcBatchItemWriterBuilder<AdvertisementDto>()
                .sql("INSERT INTO advertisement_daily_profit(advertisement_number, calculated_date, daily_views, daily_profit) VALUES(:number, :calculatedDate, :dailyViews, :dailyProfit)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    @StepScope
    public CompositeItemWriter advertisementCompositeWriter() {
        List<JdbcBatchItemWriter> writers = List.of(initializeAdvertisementTempDailyViewsWriter(), updateAdvertisementDailyProfitWriter());
        return new CompositeItemWriterBuilder()
                .delegates(writers).build();
    }
}
