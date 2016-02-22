package com.arrggh.eve.blueprint.model;

import com.arrggh.eve.blueprint.utilities.DateTimeUtilities;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
public class EvePriceHistory {
    private String totalCount_str;
    private long totalCount;
    private List<EvePriceHistoryItem> items;
    private long pageCount;
    private String pageCount_str;


    public Optional<Double> findLatestPrice() {
        if (items.size() == 0) {
            return Optional.empty();
        }
        EvePriceHistoryItem currentItem = items.get(0);
        LocalDateTime currentTime = DateTimeUtilities.parseDateTime(currentItem.getDate());

        for (EvePriceHistoryItem item : items) {
            LocalDateTime newTime = DateTimeUtilities.parseDateTime(item.getDate());
            if (newTime.isAfter(currentTime)) {
                currentItem = item;
                currentTime = newTime;
            }
        }
        return Optional.of(currentItem.getAvgPrice());
    }
}
