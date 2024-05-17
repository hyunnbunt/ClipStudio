package clipstudio.mapper;

import clipstudio.dto.AdvertisementDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Slf4j
@Component
public class AdvertisementMapper implements RowMapper<AdvertisementDto> {
    @Override
    public AdvertisementDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        log.info("inside videoMapper");
        AdvertisementDto advertisementDto = AdvertisementDto.builder()
                .number(rs.getLong("number"))
                .orderInVideo(rs.getInt("order_in_video"))
                .tempDailyViews(rs.getLong("temp_daily_views"))
                .totalViews(rs.getLong("total_views"))
                .build();
        log.info(String.valueOf(advertisementDto.getTempDailyViews()));
        return advertisementDto;
    }
}
