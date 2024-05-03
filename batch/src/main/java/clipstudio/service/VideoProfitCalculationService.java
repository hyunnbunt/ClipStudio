package clipstudio.service;

import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.SimpleJobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.dao.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class VideoProfitCalculationService {
    private final JobLauncher jobLauncher;
    private final JdbcJobInstanceDao jobInstanceDao;
    private final JdbcJobExecutionDao jdbcJobExecutionDao;
    private final JdbcStepExecutionDao jdbcStepExecutionDao;
    private final JdbcExecutionContextDao jdbcExecutionContextDao;
    private final Job job;

    public VideoProfitCalculationService(JobLauncher jobLauncher,
                                         Job job,
                                         JdbcJobInstanceDao jobInstanceDao,
                                         JdbcJobExecutionDao jdbcJobExecutionDao,
                                         JdbcStepExecutionDao jdbcStepExecutionDao,
                                         JdbcExecutionContextDao jdbcExecutionContextDao) {
        this.jobLauncher = jobLauncher;
        this.job = job;
        this.jobInstanceDao = jobInstanceDao;
        this.jdbcJobExecutionDao = jdbcJobExecutionDao;
        this.jdbcStepExecutionDao = jdbcStepExecutionDao;
        this.jdbcExecutionContextDao = jdbcExecutionContextDao;
    }

    public void runBatchJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("timestamp", String.valueOf(System.currentTimeMillis())) // JobParameter에 고유한 값 추가
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
            BatchStatus status = jobExecution.getStatus();
            System.out.println("Batch Job Status : " + status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 이거 필요한가? 잘 돌아갔는지 확인?
    public List<JobExecution> getJobExecutions() {
        JobExplorer jobExplorer = new SimpleJobExplorer(
                jobInstanceDao,
                jdbcJobExecutionDao,
                jdbcStepExecutionDao,
                jdbcExecutionContextDao
        );

        return jobExplorer.getJobExecutions(
                Objects.requireNonNull(jobInstanceDao.getLastJobInstance("job")));
    }
}
