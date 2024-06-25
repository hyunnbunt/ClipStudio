package clipstudio.util;

import clipstudio.dto.AdvertisementDto;
import clipstudio.dto.VideoDto;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ProfitCalculator {
    public Long start;
    public Long end;
    public Float price;
    public static List<Map<String, Object>> getPriceTable(long[] views, float[] won) {
        List<Map<String, Object>> list = new LinkedList<>();
        for (int i = 0; i < views.length-1; i ++) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("start", views[i]);
            temp.put("end", views[i+1]);
            temp.put("won", won[i]);
            list.add(temp);
        }
        return list;
    }

    public static double getDailyProfit(Long prevTotal, Long daily, String type) throws Exception {
        List<Map<String, Object>> priceTable;
        if (type.equals("video")) {
            priceTable = VideoDto.priceTable;
        } else if(type.equals("advertisement")) {
            priceTable = AdvertisementDto.priceTable;
        } else {
            throw new Exception("Profit calculation type should be video or advertisement.");
        }
        Integer[] range = getDailyViewsPriceRange(prevTotal, daily, priceTable);
        long total = prevTotal + daily;
        Integer startRangeIndex = range[0];
        Integer endRangeIndex = range[1];
        double profit = 0.0;
        if (startRangeIndex != null && endRangeIndex != null) {
            Long prev = prevTotal;
            for (int rangeIdx = startRangeIndex; rangeIdx <= endRangeIndex; rangeIdx ++) {
                Long start = (Long) priceTable.get(rangeIdx).get("start");
                Long end = (Long) priceTable.get(rangeIdx).get("end");
                Float won = (Float) priceTable.get(rangeIdx).get("won");
                if (rangeIdx == endRangeIndex) {
                    profit += (total - start) * (won);
                    break;
                }
                profit += (end - prev) * (won);
                prev = end;
            }
        }
        return profit;
    }
    public static Integer[] getDailyViewsPriceRange(Long prevTotal, Long daily, List<Map<String, Object>> priceTable) {
        long total = prevTotal + daily;
        Integer[] range = new Integer[2];
        for (int idx = 0; idx < priceTable.size(); idx ++) {
            Map<String, Object> temp = priceTable.get(idx);
            Long start = (Long) temp.get("start");
            Long end = (Long) temp.get("end");
            if (range[0] == null && prevTotal >= start && prevTotal < end) {
                range[0] = idx;
            }
            if (total >= start && total < end) {
                range[1] = idx;
                break;
            }
        }
        return range;
    }
}
