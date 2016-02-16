package com.arrggh.eve.blueprint.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.apache.logging.log4j.LogManager.getLogger;

public class PriceQuery {
    private static final Logger LOG = getLogger(PriceQuery.class);
    private static final Random random = new Random(System.nanoTime());
    private static final ObjectMapper mapper = new ObjectMapper();

    private static final Map<Integer, Set<PriceCacheEntry>> cache = new HashMap<>();

    public PriceQuery() {
    }

    public void loadCacheFile(File priceCache) throws IOException {
        if (priceCache.exists() && priceCache.canRead()) {
            System.out.print("Loading prices from cache ... ");
            PriceCacheEntryFile prices = mapper.readValue(priceCache, PriceCacheEntryFile.class);
            for (PriceCacheEntry entry : prices.getPrices()) {
                addEntryToCache(entry);
            }
            System.out.println("done (" + cache.size() + " prices loaded)");
        } else {
            System.out.print("Cache file does not exist ... ");
        }
    }

    public void saveCacheFile(File priceCache) throws IOException {
        System.out.print("Saving prices from cache ... ");
        List<PriceCacheEntry> prices = new LinkedList<>();
        for (Set<PriceCacheEntry> entrySet : cache.values()) {
            prices.addAll(entrySet);
        }
        PriceCacheEntryFile priceCacheFile = PriceCacheEntryFile.builder().prices(prices).build();
        mapper.writerWithDefaultPrettyPrinter().writeValue(priceCache, priceCacheFile);
        System.out.println("done (" + prices.size() + " prices saved)");
    }

    private static final int marketId = 0;

    public float queryPrice(int typeId) {
        Set<PriceCacheEntry> cached = cache.get(typeId);

        // Get it from the cache if possible
        if (cached != null) {
            for (PriceCacheEntry entry : cached) {
                if (entry.getMarketId() == marketId) {
                    return entry.getPrice();
                }
            }
        }

        // Do a lookup otherwise
        PriceCacheEntry entry = PriceCacheEntry.builder().typeId(typeId).marketId(marketId).price(random.nextFloat()).build();
        addEntryToCache(entry);
        System.out.println("Cache " + cache.size());
        return entry.getPrice();
    }

    private void addEntryToCache(PriceCacheEntry entry) {
        Set<PriceCacheEntry> entrySet = cache.getOrDefault(entry.getTypeId(), new HashSet<>());
        entrySet.add(entry);
        cache.put(entry.getTypeId(), entrySet);
    }
}
