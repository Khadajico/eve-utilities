package com.arrggh.eve.blueprint.data;

import com.arrggh.eve.blueprint.model.MarketPrice;
import com.arrggh.eve.blueprint.model.MarketPriceFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FileMarketPriceCache implements MarketPriceCache {
    private static final Map<Integer, Map<Integer, MarketPrice>> cache = new HashMap<>();
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public MarketPrice getPrice(int marketId, int typeId) {
        Map<Integer, MarketPrice> marketPrices = cache.get(typeId);
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

    @Override
    public void addPrice(int marketId, int typeId, MarketPrice entry) {
        Map<Integer, MarketPrice> marketPrices = cache.get(marketId);
        if (marketPrices == null) {
            marketPrices = new HashMap<>();
            cache.put(marketId, marketPrices);
        }
        marketPrices.put(typeId, entry);
    }

    public void loadCacheFromFile(File priceCache) throws IOException {
        if (priceCache.exists() && priceCache.canRead()) {
            System.out.print("Loading prices from cache ... ");
            MarketPriceFile prices = mapper.readValue(priceCache, MarketPriceFile.class);
            for (MarketPrice entry : prices.getPrices()) {
                addPrice(entry.getMarketId(), entry.getTypeId(), entry);
            }
            System.out.println("done (" + cache.size() + " prices loaded)");
        } else {
            System.out.println("Cache file does not exist ... done (0 prices loaded)");
        }
    }

    public void saveCacheToFile(File priceCache) throws IOException {
        System.out.print("Saving prices from cache ... ");
        List<MarketPrice> prices = new LinkedList<>();
        for (Map<Integer, MarketPrice> entrySet : cache.values()) {
            prices.addAll(entrySet.values());
        }
        MarketPriceFile priceCacheFile = MarketPriceFile.builder().prices(prices).build();
        mapper.writerWithDefaultPrettyPrinter().writeValue(priceCache, priceCacheFile);
        System.out.println("done (" + prices.size() + " prices saved)");
    }
}
