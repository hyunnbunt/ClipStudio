package clipstudio.config;

import clipstudio.dto.VideoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnThreading;
import org.springframework.boot.autoconfigure.thread.Threading;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@Slf4j
public class ExecutorServiceConfig {
    private int poolSize;
    @Value("${poolSize:20}") // 기본으로 10개의 thread 재사용
    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    @Bean
    public TaskExecutor executor() {
        // 가상 스레드 사용할 때는 threadpool 설정이 없어도 된다. 재사용 없이 계속 thread 생성 가능
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // executor.setCorePoolSize(poolSize); // 가상 스레드의 경우 의미 없음
        // executor.setMaxPoolSize(poolSize); // 가상 스레드는 거의 무제한 생성 가능
        executor.setThreadNamePrefix("multi-thread-");
        executor.setWaitForTasksToCompleteOnShutdown(Boolean.TRUE);
        executor.initialize();
        return executor;
    }

    // bean 생성 조건: virtual thread 생성시
    @Bean
    @ConditionalOnThreading(Threading.VIRTUAL)
    public ExecutorService virtualThreadExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    // bean 생성 조건: platform thread 생성시
    @Bean
    @ConditionalOnThreading(Threading.PLATFORM)
    public ExecutorService platformThreadExecutor() {
        return Executors.newCachedThreadPool();
    }


}