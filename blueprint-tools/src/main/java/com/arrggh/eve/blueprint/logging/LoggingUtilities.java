/*
 * Copyright (C) 2016 Khadajico
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package com.arrggh.eve.blueprint.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;

import static org.apache.logging.log4j.LogManager.getContext;

public interface LoggingUtilities {
    /**
     * Set the global logging level. The new log4j has deprecated the setLevel()
     * method and you now need to jump through some extra hoops to get it done.
     */
    static void setLoggingLevel(Level level) {
        LoggerContext ctx = (LoggerContext) getContext(false);
        Configuration config = ctx.getConfiguration();
        config.getRootLogger().setLevel(level);
        ctx.updateLoggers(config);
    }
}
