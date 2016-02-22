package com.arrggh.eve.blueprint.data;

import com.arrggh.eve.blueprint.model.MarketPrice;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MarketPriceCache {
    protected final Map<Integer, Map<Integer, MarketPrice>> cachedPriceMap = new HashMap<>();

    public MarketPrice getPrice(int typeId, int marketId) {
        Map<Integer, MarketPrice> marketPrices = cachedPriceMap.get(typeId);
        if (marketPrices != null) {
            MarketPrice cachedPrice = marketPrices.get(marketId);
            if (cachedPrice != null) {
                long twentyFourHoursAgo = System.nanoTime() - TimeUnit.HOURS.toNanos(24);
                if (cachedPrice.getTime() > twentyFourHoursAgo) {
                    return cachedPrice;
                } else {
                    marketPrices.remove(marketId);
                }
            }
        }
        return null;
    }

    public void addPrice(int typeId, int marketId, MarketPrice entry) {
        Map<Integer, MarketPrice> marketPrices = cachedPriceMap.get(marketId);
        if (marketPrices == null) {
            marketPrices = new HashMap<>();
            cachedPriceMap.put(marketId, marketPrices);
        }
        marketPrices.put(typeId, entry);
    }

    public Set<Integer> getTypeIds() {
        return cachedPriceMap.keySet();
    }

    public Map<Integer, MarketPrice> getAllPricesForItem(int typeId) {
        return cachedPriceMap.get(typeId);
    }
}
