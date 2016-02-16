package com.arrggh.eve.blueprint;

import com.arrggh.eve.blueprint.data.BlueprintLoader;
import com.arrggh.eve.blueprint.data.PriceQuery;
import com.arrggh.eve.blueprint.data.TypeLoader;
import com.arrggh.eve.blueprint.logging.LoggingUtilities;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.Level;

import java.io.IOException;

import static com.arrggh.eve.blueprint.cli.ArgumentProcessor.parseInteger;

public class BlueprintQuery {
    public static void main(String[] args) throws ParseException, IOException {
        System.out.println("Starting Blueprint Query");
        Options options = new Options();
        Option verbose = new Option("verbose", "be extra verbose");
        Option debug = new Option("debug", "print debugging information");
        Option limit = Option.builder("limit")
                .hasArg()
                .desc("restrict the number of search results found (default 10)")
                .build();
        Option optimize = Option.builder("optimize")
                .hasArg()
                .desc("optimise the build options for the blueprint")
                .build();
        Option locate = Option.builder("locate")
                .hasArg()
                .desc("locate a blueprint using the substring")
                .build();

        options.addOption(limit);
        options.addOption(verbose);
        options.addOption(debug);
        options.addOption(optimize);
        options.addOption(locate);

        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);

        if (line.hasOption(debug.getOpt())) {
            LoggingUtilities.setLoggingLevel(Level.INFO);
        }
        if (line.hasOption(verbose.getOpt())) {
            LoggingUtilities.setLoggingLevel(Level.ALL);
        }

        BlueprintLoader blueprintLoader = new BlueprintLoader();
        TypeLoader typeLoader = new TypeLoader();
        PriceQuery priceQuery = new PriceQuery();

        if (line.hasOption(optimize.getOpt())) {
            blueprintLoader.loadFile();
            typeLoader.loadFile();
            String blueprintName = line.getOptionValue(optimize.getOpt());
            BlueprintOptimizer optimizer = new BlueprintOptimizer(typeLoader, blueprintLoader, priceQuery, blueprintName);
            optimizer.optimize();
        } else if (line.hasOption(locate.getOpt())) {
            blueprintLoader.loadFile();
            String searchString = line.getOptionValue(locate.getOpt());
            Integer resultLimit = parseInteger("limit", line.getOptionValue(limit.getOpt()), 10);
            BlueprintLocator locator = new BlueprintLocator(blueprintLoader, searchString, resultLimit);
            locator.locate();
        } else {
            String header = "Perform blueprint build optimizations\n\n";
            String footer = "\nPlease report any issues to khadajico (at) gmail (dot) com";

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar blueprint-query.jar", header, options, footer, true);        }
    }
}
