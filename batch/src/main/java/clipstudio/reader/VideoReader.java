package clipstudio.reader;

import clipstudio.Entity.Video;
import clipstudio.oauth2.User.entity.User;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

public class VideoReader implements ItemReader<Video> {

    DataSource dataSource;
    @Autowired
    public VideoReader(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public JdbcCursorItemReader<Video> itemReader() {
        return new JdbcCursorItemReaderBuilder<Video>()
                .name("videoReader")
                .dataSource(dataSource)
                .rowMapper((rs, rowNum) -> Video.builder()
                        .title(rs.getString("title"))
                        .number(rs.getLong("number"))
                        .uploader((User) rs.getObject("user"))
                        .createdDate(rs.getTimestamp("created_date"))
                        .duration(rs.getInt("duration"))
                        .dailyViews(rs.getLong("daily_views"))
                        .totalViews(rs.getLong("total_views")).build())
                .sql("SELECT * FROM videos WHERE number=1") // rowMapper()는 db에서 받아온 결과를 Java 객체로 매핑하는 구현체. 지정 가능.
                .build();
    }

    @Override
    public Video read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        return null;
    }
}
