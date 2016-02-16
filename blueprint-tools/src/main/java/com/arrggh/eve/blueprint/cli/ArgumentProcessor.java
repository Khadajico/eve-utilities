package com.arrggh.eve.blueprint.cli;


import com.arrggh.eve.blueprint.logging.LoggingUtilities;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArgumentProcessor {
    private static final Logger LOG = LogManager.getLogger(ArgumentProcessor.class);

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

