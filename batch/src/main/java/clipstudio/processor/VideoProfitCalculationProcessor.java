package clipstudio.processor;

import clipstudio.dto.DailyProfitOfVideo;
import clipstudio.dto.DailyViews;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class VideoProfitCalculationProcessor implements ItemProcessor<DailyViews, DailyProfitOfVideo> {

    @Override
    public DailyProfitOfVideo process(DailyViews dailyViews) throws Exception {
        log.info(String.valueOf(dailyViews));
        final Long total = dailyViews.getTotalViews();
        final Long daily = dailyViews.getDailyViews();
        log.info("video total views: " + total);
        log.info("video daily views: " + daily);
        // should implement daily profit calculation code here.
        List<Map<String, Object>> priceTable = DailyProfitOfVideo.priceTable;
        int startRangeIdx = -1;
        int endRangeIdx = -1;
        for (int idx = 0; idx < priceTable.size(); idx ++) {
            Map<String, Object> temp = priceTable.get(idx);
            if (total >= (Long) temp.get("start") && total < (Long) temp.get("end")) {
                startRangeIdx = idx;
            }
            if (total + daily >= (Long) temp.get("start") && total + daily < (Long) temp.get("end")) {
                endRangeIdx = idx;
                break;
            }
        }
        double profit = 0.0;
        if (startRangeIdx != -1 && endRangeIdx != -1) {
            Long curr = total;
            for (int rangeIdx = startRangeIdx; rangeIdx <= endRangeIdx; rangeIdx ++) {
                if (rangeIdx == endRangeIdx) {
                    profit += (total + daily - (Long) priceTable.get(rangeIdx).get("start")) * ((Float) priceTable.get(rangeIdx).get("won"));
                    log.info("profit: " + profit);
                } else {
                    profit += ((Long) priceTable.get(rangeIdx).get("end") - curr) * ((Float) priceTable.get(rangeIdx).get("won"));
                    curr = (Long) priceTable.get(rangeIdx).get("end");
                    log.info("profit: " + profit);
                }
            }
        }
        return DailyProfitOfVideo.builder()
                .videoNumber(dailyViews.getNumber())
                .dailyProfit(profit) // dummy data for testing.
                .calculatedDate(new Date()).build();
    }
}