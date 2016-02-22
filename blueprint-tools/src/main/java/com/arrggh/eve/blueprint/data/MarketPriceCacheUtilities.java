package com.arrggh.eve.blueprint.data;

import com.arrggh.eve.blueprint.model.MarketPrice;
import com.arrggh.eve.blueprint.model.MarketPriceFile;
import com.arrggh.eve.blueprint.utilities.ObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class MarketPriceCacheUtilities {
    private static final ObjectMapper mapper = ObjectMapperFactory.buildObjectMapper();

    private MarketPriceCacheUtilities() {
    }

    public static void loadCacheFromFile(MarketPriceCache cache, File priceCache) throws IOException {
        if (priceCache.exists() && priceCache.canRead()) {
            loadCacheFromURL(cache, priceCache.toURI().toURL());
        } else {
            System.out.println("Cache file does not exist ... done (0 prices loaded)");
        }
    }

    public static void saveCacheToFile(MarketPriceCache cache, File priceCache) throws IOException {
        System.out.print("Saving prices from cache ... ");
        List<MarketPrice> prices = new LinkedList<>();
        for (int typeId : cache.getTypeIds()) {
            prices.addAll(cache.getAllPricesForItem(typeId).values());
        }
        MarketPriceFile priceCacheFile = MarketPriceFile.builder().prices(prices).build();
        mapper.writerWithDefaultPrettyPrinter().writeValue(priceCache, priceCacheFile);
        System.out.println("done (" + prices.size() + " prices saved)");
    }

    public static void loadCacheFromURL(MarketPriceCache cache, URL url) throws IOException {
        System.out.print("Loading prices from cache ... ");
        MarketPriceFile prices = mapper.readValue(url, MarketPriceFile.class);
        int count = 0;
        for (MarketPrice entry : prices.getPrices()) {
            cache.addPrice(entry.getMarketId(), entry.getTypeId(), entry);
            count++;
        }
        System.out.println("done (" + count + " prices loaded)");

    }
}
