package clipstudio.mapper;

import clipstudio.dto.Video;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Slf4j
@Component
public class VideoMapper implements RowMapper<Video> {
    @Override
    public Video mapRow(ResultSet rs, int rowNum) throws SQLException {
        log.info("inside videoMapper");
        Video video = Video.builder()
                .title(rs.getString("title"))
                .number(rs.getLong("number"))
                .createdDate(LocalDate.parse(rs.getString("created_date")))
                .durationSec(rs.getInt("duration_sec"))
                .tempDailyViews(rs.getLong("temp_daily_views"))
                .totalViews(rs.getLong("total_views"))
                .build();
        log.info(String.valueOf(video.getTempDailyViews()));
        return video;
    }

}
