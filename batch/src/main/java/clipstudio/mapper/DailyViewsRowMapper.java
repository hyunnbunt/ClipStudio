package clipstudio.mapper;

import clipstudio.dto.DailyViews;
import clipstudio.oauth2.User.entity.User;
import org.springframework.jdbc.core.RowMapper;

import javax.swing.tree.TreePath;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DailyViewsRowMapper implements RowMapper<DailyViews> {
    @Override
    public DailyViews mapRow(ResultSet rs, int rowNum) throws SQLException {
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
