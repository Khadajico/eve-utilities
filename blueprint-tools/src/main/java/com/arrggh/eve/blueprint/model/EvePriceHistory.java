package com.arrggh.eve.blueprint.model;

import com.arrggh.eve.blueprint.utilities.DateTimeUtilities;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * CREST Market Price History
 * <p>
 * This is the POJO that is populated by parsing the JSON object that is returned when
 * a CREST price query is executed.
 */
@Data
public class EvePriceHistory {
    private String totalCount_str;
    private long totalCount;
    private long pageCount;
    private String pageCount_str;
    private List<EvePriceHistoryItem> items;

    /**
     * Search all the Eve Price History items looking for the latest market price (as ordered
     * by date) and return the average price.
     * <p>
     * Note: This function will return Optional.empty() if there are no price entries found,
     * which can happen if there has never been any of the item sold on the market (e.g. the
     * Avatar titan).
     *
     * @return The latest price (if found).
     */
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
