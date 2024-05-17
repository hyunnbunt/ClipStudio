package clipstudio.mapper;

import clipstudio.dto.VideoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Component
public class VideoMapper implements RowMapper<VideoDto> {
    @Override
    public VideoDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        log.info("inside videoMapper");
        VideoDto video = VideoDto.builder()
                .title(rs.getString("title"))
                .number(rs.getLong("number"))
                .durationSec(rs.getInt("duration_sec"))
                .tempDailyViews(rs.getLong("temp_daily_views"))
                .totalViews(rs.getLong("total_views"))
                .build();
        log.info(String.valueOf(video.getTempDailyViews()));
        return video;
    }

}
