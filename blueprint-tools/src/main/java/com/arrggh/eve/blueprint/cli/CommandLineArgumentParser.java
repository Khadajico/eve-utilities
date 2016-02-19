package com.arrggh.eve.blueprint.cli;


import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandLineArgumentParser {
    private static final Logger LOG = LogManager.getLogger(CommandLineArgumentParser.class);

    private static final String VERBOSE = "verbose";
    private static final String DEBUG = "debug";
    private static final String LIMIT = "limit";
    private static final String OPTIMIZE = "optimize";
    private static final String LOCATE = "locate";
    private static final String PRICE_CACHE = "priceCache";
    private static final String VERSION = "version";

    private final Options options = new Options();

    public CommandLineArgumentParser() {
        options.addOption(Option.builder(LIMIT).hasArg().desc("restrict the number of search results found (default 10)").build());
        options.addOption(new Option(VERBOSE, "be extra verbose"));
        options.addOption(new Option(DEBUG, "print debugging information"));
        options.addOption(Option.builder(OPTIMIZE).hasArg().desc("optimise the build options for the blueprint").build());
        options.addOption(Option.builder(LOCATE).hasArg().desc("locate a blueprint using the supplied substring").build());
        options.addOption(Option.builder(PRICE_CACHE).hasArg().desc("use the supplied price cache file (default price-cache.json)").build());
        options.addOption(new Option(VERSION, "show version information"));
    }

    public Parameters parseArguments(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);

        boolean debug = line.hasOption(DEBUG);
        boolean verbose = line.hasOption(VERBOSE);
        boolean version = line.hasOption(VERSION);

        boolean optimize = line.hasOption(OPTIMIZE);
        boolean locate = line.hasOption(LOCATE);
        boolean cache = line.hasOption(PRICE_CACHE);

        String blueprintName = optimize ? line.getOptionValue(OPTIMIZE) : "";
        String searchString = locate ? line.getOptionValue(LOCATE) : "";
        String priceCache = cache ? line.getOptionValue(PRICE_CACHE) : "price-cache.json";

        int limit = parseInteger(LIMIT, line.getOptionValue(LIMIT), 10);

        return Parameters.builder().debug(debug).verbose(verbose)
                .optimize(optimize).blueprintName(blueprintName).priceCache(priceCache)
                .locate(locate).searchString(searchString).limit(limit)
                .version(version)
                .build();
    }

    public void dumpHelpToConsole() {
        String header = "Perform blueprint build optimizations\n\n";
        String footer = "\nPlease report any issues to khadajico (at) gmail (dot) com";

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar blueprint-query.jar", header, options, footer, true);
    }

    public static int parseInteger(String name, String value, int defaultValue) {
        if (value == null || value.trim().isEmpty())
            return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOG.error("Command line option '" + name + "', cannot parse '" + value + "' as a number ... returning default of " + defaultValue);
        }
        return defaultValue;
    }
}

