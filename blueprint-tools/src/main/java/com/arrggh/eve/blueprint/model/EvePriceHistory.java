package com.arrggh.eve.blueprint.model;

import com.arrggh.eve.blueprint.utilities.DateTimeUtilities;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EvePriceHistory {
    private String totalCount_str;
    private long totalCount;
    private List<EvePriceHistoryItem> items;
    private long pageCount;
    private String pageCount_str;


    public double findLatestPrice() {
        EvePriceHistoryItem currentItem = items.get(0);
        LocalDateTime currentTime = DateTimeUtilities.parseDateTime(currentItem.getDate());

        for (EvePriceHistoryItem item : items) {
            LocalDateTime newTime = DateTimeUtilities.parseDateTime(item.getDate());
            if (newTime.isAfter(currentTime)) {
                currentItem = item;
                currentTime = newTime;
            }
        }
        return currentItem.getAvgPrice();
    }
}
