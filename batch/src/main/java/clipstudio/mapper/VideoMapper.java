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
        VideoDto video = VideoDto.builder()
                .title(rs.getString("title"))
                .number(rs.getLong("number"))
                .durationSec(rs.getInt("duration_sec"))
                .dailyPlayedSec(rs.getLong("today_played_sec"))
                .todayViews(rs.getLong("today_views"))
                .totalViews(rs.getLong("total_views"))
                .build();
        log.info(String.valueOf(video.getTodayViews()));
        return video;
    }

}
