package com.arrggh.eve.blueprint.optimizer;

import com.arrggh.eve.blueprint.data.BlueprintLoader;
import com.arrggh.eve.blueprint.data.MarketPriceCacheUtilities;
import com.arrggh.eve.blueprint.data.PriceQuery;
import com.arrggh.eve.blueprint.data.TypeLoader;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class BlueprintOptimizerTest {
    private static TypeLoader typeLoader;
    private static BlueprintLoader blueprintLoader;
    private static TestMarketPriceCache priceCache;
    private static PriceQuery priceQuery;

    @BeforeClass
    public static void loadReferenceData() throws IOException {
        typeLoader = new TypeLoader();
        blueprintLoader = new BlueprintLoader();

        typeLoader.loadFile();
        blueprintLoader.loadFile();

        priceCache = new TestMarketPriceCache();
        priceQuery = new TestPriceQuery(priceCache);
    }

    @Test
    public void testOptimizeVespaII() throws IOException {
        MarketPriceCacheUtilities.loadCacheFromURL(priceCache, BlueprintOptimizerTest.class.getResource("/vespaII-price-cache.json"));
        priceCache.resetCacheTimestamps();

        BlueprintOptimizer optimizer = new BlueprintOptimizer(typeLoader, blueprintLoader, priceQuery, "Vespa II Blueprint");
        BuildManifest manifest = optimizer.generateBuildTree();
        manifest.dumpToConsole();
    }

    @Test
    public void testOptimizeAvatar() throws IOException {
        MarketPriceCacheUtilities.loadCacheFromURL(priceCache, BlueprintOptimizerTest.class.getResource("/avatar-price-cache.json"));
        priceCache.resetCacheTimestamps();

        BlueprintOptimizer optimizer = new BlueprintOptimizer(typeLoader, blueprintLoader, priceQuery, "Avatar Blueprint");
        BuildManifest manifest = optimizer.generateBuildTree();
        manifest.dumpToConsole();
    }
}