/*
 * Copyright (C) 2016 Khadajico
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package com.arrggh.eve.blueprint;

import com.arrggh.eve.blueprint.cli.CommandLineArgumentParser;
import com.arrggh.eve.blueprint.cli.Parameters;
import com.arrggh.eve.blueprint.data.*;
import com.arrggh.eve.blueprint.logging.LoggingUtilities;
import com.arrggh.eve.blueprint.optimizer.BlueprintOptimizerAction;
import com.arrggh.eve.blueprint.optimizer.BuildManifest;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BlueprintTool {
    public static void main(String[] args) throws ParseException, IOException {
        CommandLineArgumentParser parser = new CommandLineArgumentParser();
        Parameters parameters = parser.parseArguments(args);

        if (parameters.isVerbose()) {
            LoggingUtilities.setLoggingLevel(Level.ALL);
        } else if (parameters.isDebug()) {
            LoggingUtilities.setLoggingLevel(Level.INFO);
        } else {
            LoggingUtilities.setLoggingLevel(Level.WARN);
        }

        MarketPriceCache priceCache = new MarketPriceCache();
        BlueprintLoader blueprintLoader = new BlueprintLoader();
        TypeLoader typeLoader = new TypeLoader();
        PriceQuery priceQuery = new PriceQuery(priceCache);

        if (parameters.isOptimize()) {
            blueprintLoader.loadFile();
            typeLoader.loadFile();
            File priceCacheFile = new File(parameters.getPriceCache());
            MarketPriceCacheUtilities.loadCacheFromFile(priceCache, priceCacheFile);
            BlueprintOptimizerAction optimizer = new BlueprintOptimizerAction(typeLoader, blueprintLoader, priceQuery, parameters.getBlueprintName());
            BuildManifest manifest = optimizer.generateBuildTree();
            manifest.dumpToConsole();
            MarketPriceCacheUtilities.saveCacheToFile(priceCache, priceCacheFile);
        } else if (parameters.isLocate()) {
            blueprintLoader.loadFile();
            List<String> results = blueprintLoader.matchBlueprintNames(parameters.getSearchString(), parameters.getLimit());
            results.forEach(blueprintName -> System.out.println("  " + blueprintName));
        } else if (parameters.isVersion()) {
            parser.dumpVersionInfoToConsole();
        } else {
            parser.dumpHelpToConsole();
        }
    }
}
