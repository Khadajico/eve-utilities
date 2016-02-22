package com.arrggh.eve.blueprint.optimizer;

import com.arrggh.eve.blueprint.data.MarketPriceCache;
import com.arrggh.eve.blueprint.model.MarketPrice;

import java.util.HashMap;
import java.util.Map;

public class TestMarketPriceCache extends MarketPriceCache {
    public void resetCacheTimestamps() {
        long now = System.nanoTime();
        for (int typeId : cachedPriceMap.keySet()) {
            Map<Integer, MarketPrice> resetMarketPrices = new HashMap<>();
            Map<Integer, MarketPrice> cachedMarketPrices = cachedPriceMap.get(typeId);
            for (int marketId : cachedMarketPrices.keySet()) {
                MarketPrice cachedMarketPrice = cachedMarketPrices.get(marketId);
                MarketPrice resetMarketPrice = MarketPrice.builder().typeId(cachedMarketPrice.getTypeId()).marketId(cachedMarketPrice.getMarketId()).price(cachedMarketPrice.getPrice()).time(now).build();
                resetMarketPrices.put(marketId, resetMarketPrice);
            }
            cachedPriceMap.put(typeId, resetMarketPrices);
        }
    }
}
