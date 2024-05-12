package clipstudio.mapper;

import clipstudio.dto.Advertisement;
import clipstudio.dto.Video;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Slf4j
@Component
public class AdvertisementMapper implements RowMapper<Advertisement> {
    @Override
    public Advertisement mapRow(ResultSet rs, int rowNum) throws SQLException {
        log.info("inside videoMapper");
        Advertisement advertisement = Advertisement.builder()
                .number(rs.getLong("number"))
                .orderInVideo(rs.getInt("order_in_video"))
                .tempDailyViews(rs.getLong("temp_daily_views"))
                .totalViews(rs.getLong("total_views"))
                .build();
        log.info(String.valueOf(advertisement.getTempDailyViews()));
        return advertisement;
    }
}
