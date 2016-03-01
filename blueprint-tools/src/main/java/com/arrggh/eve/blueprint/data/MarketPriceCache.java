/*
 * Copyright (C) 2016 Khadajico
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package com.arrggh.eve.blueprint.data;

import com.arrggh.eve.blueprint.model.MarketPrice;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MarketPriceCache {
    protected final Map<Integer, Map<Integer, MarketPrice>> cachedPriceMap = new HashMap<>();

    /**
     * Returned the cached market price, if there is:
     * <p>
     * <ul>
     * <li>An entry in the cache for the typeId / marketId pair</li>
     * <li>The entry is less than 24 hours old</li>
     * </ul>
     * <p>
     * If an entry cannot be found then an Optional.empty() will be returns.
     */
    public Optional<MarketPrice> getPrice(int typeId, int marketId) {
        Map<Integer, MarketPrice> marketPrices = cachedPriceMap.get(typeId);
        if (marketPrices != null) {
            MarketPrice cachedPrice = marketPrices.get(marketId);
            if (cachedPrice != null) {
                long twentyFourHoursAgo = System.nanoTime() - TimeUnit.HOURS.toNanos(24);
                if (cachedPrice.getTime() > twentyFourHoursAgo) {
                    return Optional.of(cachedPrice);
                } else {
                    marketPrices.remove(marketId);
                }
            }
        }
        return Optional.empty();
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
