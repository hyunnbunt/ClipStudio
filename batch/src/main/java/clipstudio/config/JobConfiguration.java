package clipstudio.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import clipstudio.Entity.DailyProfitOfVideo;
import clipstudio.Entity.Video;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing(dataSourceRef = "batchDataSource", transactionManagerRef = "batchTransactionManager")
public class JobConfiguration {
    /**
     * Note the JobRepository is typically autowired in and not needed to be explicitly
     * configured
     */
    @Autowired
    public JobRepository jobRepository;
    @Autowired
    public JobLauncher jobLauncher;
    @Autowired DataSource batchDataSource;
    @Autowired PlatformTransactionManager transactionManager;

    @Bean
    private DataSource batchDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.valueOf("mariadb")).build();
    }

    @Bean
    private JdbcTransactionManager batchTransactionManager(DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }

    @Bean
    public Job job(JobRepository jobRepository, Step step) {
        return new JobBuilder("job", jobRepository)
                .start(step) // step (순서대로)
//                .next()
//                .next()
                .build();
    }
    @Bean
    public Step step(JobRepository jobRepository) {
        return new StepBuilder("step", jobRepository)
                .<Video, DailyProfitOfVideo>chunk(10, transactionManager)
                //	transactionManager: Spring’s PlatformTransactionManager that begins
                //	and commits transactions during processing.
                .reader(itemReader())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Video> itemReader() {
        return new JdbcCursorItemReaderBuilder<Video>()
                .name("videoReader")
                .dataSource(batchDataSource)
                .sql("SELECT * FROM videos WHERE number=1")
                .build();
    }
    @Bean
    public JdbcBatchItemWriter<DailyProfitOfVideo> itemWriter() {
        /**
         * The JdbcBatchItemWriter is an ItemWriter that uses the batching features
         * from NamedParameterJdbcTemplate to execute a batch of statements
         * for all items provided. Spring Batch provides a JdbcBatchItemWriterBuilder
         * to construct an instance of the JdbcBatchItemWriter.
        */
        return new JdbcBatchItemWriterBuilder<DailyProfitOfVideo>()
                .dataSource(batchDataSource)
                .sql("INSERT INTO TABLE daily_profit_of_video(calculated_date, daily_profit) VALUES(current_date(), 99)")
                .build();
    }
}

