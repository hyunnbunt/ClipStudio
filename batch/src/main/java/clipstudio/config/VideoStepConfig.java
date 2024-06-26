package clipstudio.config;

import clipstudio.dto.VideoDto;
import clipstudio.mapper.VideoMapper;
import clipstudio.processor.VideoProfitProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
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
public class VideoStepConfig {

    private final DataSource dataSource;
    private final VideoMapper videoMapper;
    private final VideoProfitProcessor videoProfitProcessor;
    private final ExecutorServiceConfig executorServiceConfig;
    private final Executor virtualThreadExecutor;
    @Bean
    @JobScope // 빈의 생성 시점을 지정된 Scope가 실행되는 시점으로 지연 (late binding), 동일한 컴포넌트를 병렬 처리할 때 유리
    public Step videoProfitStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                @Value("#{jobParameters[batchDate]}") String batchDate) throws ParseException {
        log.info(String.valueOf(new SimpleDateFormat("yyyy-MM-dd").parse(batchDate)));
        return new StepBuilder("videoProfitStep", jobRepository)
                .<VideoDto, VideoDto>chunk(2000, transactionManager) // test if the result changes by chunk size. TransactionManager: Spring’s PlatformTransactionManager that begins and commits transactions during processing.
                .reader(syncVideoReader())
//                .listener(new ReadListener()) // reader 동작 테스트
                // .allowStartIfComplete(true) // test environment, 중복 실행 허용
                .processor(videoProfitProcessor)
//                .writer(videoCompositeWriter())
                .writer(updateVideoDailyProfitWriter()) // test environment, 같은 데이터 활용을 위해 초기화하지 않음.
                .taskExecutor(
                        new ConcurrentTaskExecutor(
                                virtualThreadExecutor //가상 스레드
                        ))
                .taskExecutor(executorServiceConfig.executor())
                .build();
    }


    @Bean
    @StepScope
    public JdbcCursorItemReader<VideoDto> videoReader() { // 싱글 스레드 상황에서 사용
        JdbcCursorItemReader<VideoDto> reader = new JdbcCursorItemReaderBuilder<VideoDto>()
                .name("videoReader")
                .dataSource(dataSource)
                .rowMapper(videoMapper)
                .sql("SELECT * FROM videos")
                .build();
        return reader;
    }

    @Bean
    @StepScope
    public SynchronizedItemStreamReader<VideoDto> syncVideoReader() { // multi-thread 상황에서 사용
        JdbcCursorItemReader<VideoDto> reader = new JdbcCursorItemReaderBuilder<VideoDto>()
                .name("videoReader")
                .dataSource(dataSource)
                .rowMapper(videoMapper)
                .sql("SELECT * FROM videos")
                .build();
        return new SynchronizedItemStreamReaderBuilder<VideoDto>()
                .delegate(reader)
                .build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<VideoDto> initializeVideoTempDailyViewsWriter() {
        return new JdbcBatchItemWriterBuilder<VideoDto>()
                .sql("UPDATE videos SET today_views=:todayViews, today_played_sec=0, total_views=:totalViews where number=:number")
                .dataSource(dataSource)
                .beanMapped() // ?? https://jojoldu.tistory.com/339
                .build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<VideoDto> updateVideoDailyProfitWriter() {
        return new JdbcBatchItemWriterBuilder<VideoDto>()
                .sql("INSERT INTO total_profit(uploader_number, video_number, date, views, played_sec, video_profit, advertisements_profit) " +
                        "VALUES(:uploaderNumber, :number, :date, :todayViews, :todayPlayedSec, :videoProfit, :advertisementsProfit)")
                .dataSource(dataSource)
                .beanMapped() // ?? https://jojoldu.tistory.com/339
                .build();
    } // JpaItemWriter?

    @Bean
    @StepScope
    public CompositeItemWriter videoCompositeWriter() {
        List<JdbcBatchItemWriter> writers = List.of(initializeVideoTempDailyViewsWriter(), updateVideoDailyProfitWriter());
        return new CompositeItemWriterBuilder()
                .delegates(writers).build();
    }

    public static class ReadListener implements ItemReadListener<VideoDto> {
        @Override
        public void afterRead(VideoDto videoDto) {
            System.out.println("Reading item number = " + videoDto.getNumber());
        }
    }
}
