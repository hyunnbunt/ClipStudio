package clipstudio.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
@Builder
public class DailyProfitOfVideo {
    Long videoNumber; // video number 와 일치
    Double dailyProfit;
    Date calculatedDate;
    public static final List<Map<String, Object>> priceTable = getPriceTable();
    private static List<Map<String, Object>> getPriceTable() {
        List<Map<String, Object>> list = new LinkedList<>();
        Long[] views = new Long[]{1L, 100000L, 500000L, 1000000L, Long.MAX_VALUE};
        Float[] won = new Float[]{1f, 1.1f, 1.3f, 1.5f};
        for (int i = 0; i < views.length-1; i ++) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("start", views[i]);
            temp.put("end", views[i+1]);
            temp.put("won", won[i]);
            list.add(temp);
        }
        return list;
    }
}
