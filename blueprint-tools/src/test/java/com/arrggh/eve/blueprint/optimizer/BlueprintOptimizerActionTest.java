/*
 * Copyright (C) 2016 Khadajico
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package com.arrggh.eve.blueprint.optimizer;

import com.arrggh.eve.blueprint.data.BlueprintLoader;
import com.arrggh.eve.blueprint.data.MarketPriceCacheUtilities;
import com.arrggh.eve.blueprint.data.PriceQuery;
import com.arrggh.eve.blueprint.data.TypeLoader;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class BlueprintOptimizerActionTest {
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
        MarketPriceCacheUtilities.loadCacheFromURL(priceCache, BlueprintOptimizerActionTest.class.getResource("/vespaII-price-cache.json"));
        priceCache.resetCacheTimestamps();

        BlueprintOptimizerAction optimizer = new BlueprintOptimizerAction(typeLoader, blueprintLoader, priceQuery, "Vespa II Blueprint");
        BuildManifest manifest = optimizer.generateBuildTree();
        manifest.dumpToConsole();
        assertEquals(3, manifest.getBuildList().size());
        assertEquals(11, manifest.getShoppingList().size());
    }

    @Test
    public void testOptimizeAvatar() throws IOException {
        MarketPriceCacheUtilities.loadCacheFromURL(priceCache, BlueprintOptimizerActionTest.class.getResource("/avatar-price-cache.json"));
        priceCache.resetCacheTimestamps();

        BlueprintOptimizerAction optimizer = new BlueprintOptimizerAction(typeLoader, blueprintLoader, priceQuery, "Avatar Blueprint");
        BuildManifest manifest = optimizer.generateBuildTree();
        manifest.dumpToConsole();
        assertEquals(6, manifest.getBuildList().size());
        assertEquals(17, manifest.getShoppingList().size());
    }
}