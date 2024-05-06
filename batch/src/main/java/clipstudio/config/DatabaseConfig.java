package clipstudio.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConfigurationProperties(prefix="spring.datasource.hikari")
@RequiredArgsConstructor
@Getter
@Setter
@Slf4j
public class DatabaseConfig {
    private String jdbcUrl;
    private String username;
    private String password;
    private String driverClassName;
    public HikariDataSource getDataSource() {
        return (HikariDataSource) DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();
    }
}
