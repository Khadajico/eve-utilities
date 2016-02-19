package com.arrggh.eve.blueprint.optimizer;

import com.arrggh.eve.blueprint.data.BlueprintLoader;
import com.arrggh.eve.blueprint.data.PriceQuery;
import com.arrggh.eve.blueprint.data.TypeLoader;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class BlueprintOptimizerTest {
    private static TypeLoader typeLoader;
    private static BlueprintLoader blueprintLoader;

    @BeforeClass
    public static void loadReferenceData() throws IOException {
        typeLoader = new TypeLoader();
        blueprintLoader = new BlueprintLoader();

        typeLoader.loadFile();
        blueprintLoader.loadFile();
    }

    @Test
    public void testOptimize() {
        PriceQuery priceQuery = new PriceQuery(new TestPriceCache());
        BlueprintOptimizer optimizer = new BlueprintOptimizer(typeLoader, blueprintLoader, priceQuery, "Vespa II Blueprint");
        optimizer.generateBuildTree();
    }
}