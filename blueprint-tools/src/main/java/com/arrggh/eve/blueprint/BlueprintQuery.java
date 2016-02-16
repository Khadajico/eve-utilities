package com.arrggh.eve.blueprint;

import com.arrggh.eve.blueprint.cli.CommandLineArgumentParser;
import com.arrggh.eve.blueprint.cli.Parameters;
import com.arrggh.eve.blueprint.data.BlueprintLoader;
import com.arrggh.eve.blueprint.data.PriceQuery;
import com.arrggh.eve.blueprint.data.TypeLoader;
import com.arrggh.eve.blueprint.locator.BlueprintLocator;
import com.arrggh.eve.blueprint.logging.LoggingUtilities;
import com.arrggh.eve.blueprint.optimizer.BlueprintOptimizer;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.Level;

import java.io.IOException;

public class BlueprintQuery {
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

        BlueprintLoader blueprintLoader = new BlueprintLoader();
        TypeLoader typeLoader = new TypeLoader();
        PriceQuery priceQuery = new PriceQuery();

        if (parameters.isOptimize()) {
            blueprintLoader.loadFile();
            typeLoader.loadFile();

            BlueprintOptimizer optimizer = new BlueprintOptimizer(typeLoader, blueprintLoader, priceQuery, parameters.getBlueprintName());
            optimizer.optimize();
        } else if (parameters.isLocate()) {
            blueprintLoader.loadFile();

            BlueprintLocator locator = new BlueprintLocator(blueprintLoader, parameters.getSearchString(), parameters.getLimit());
            locator.locate();
        } else {
            parser.dumpHelpToConsole();
        }
    }
}
