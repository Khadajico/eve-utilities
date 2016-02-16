package com.arrggh.eve.blueprint.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;

import static org.apache.logging.log4j.LogManager.getContext;

public interface LoggingUtilities {
    static void setLoggingLevel(Level level) {
        LoggerContext ctx = (LoggerContext) getContext(false);
        Configuration config = ctx.getConfiguration();
        config.getRootLogger().setLevel(level);
        ctx.updateLoggers(config);
    }
}
