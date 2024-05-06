package clipstudio.mapper;

import clipstudio.dto.DailyViews;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Component
public class VideoMapper implements RowMapper<DailyViews> {
    @Override
    public DailyViews mapRow(ResultSet rs, int rowNum) throws SQLException {
        log.info("inside videoMapper");
        return DailyViews.builder()
                .title(rs.getString("title"))
                .number(rs.getLong("number"))
                .createdDate(rs.getTimestamp("created_date"))
                .duration(rs.getInt("duration"))
                .dailyViews(rs.getLong("daily_views"))
                .totalViews(rs.getLong("total_views"))
                .build();
    }

}
