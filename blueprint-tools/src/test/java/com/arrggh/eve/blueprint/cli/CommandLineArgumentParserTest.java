package com.arrggh.eve.blueprint.cli;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommandLineArgumentParserTest {
    private final CommandLineArgumentParser parser = new CommandLineArgumentParser();

    @Test
    public void testParseArgumentsForLocate() throws Exception {
        Parameters parameters = parser.parseArguments(new String[]{"-limit", "20", "-locate", "blueprint"});
        assertNotNull(parameters);
        assertEquals(20, parameters.getLimit());
        assertFalse(parameters.isOptimize());
        assertTrue(parameters.isLocate());
        assertEquals("blueprint", parameters.getSearchString());
    }

    @Test
    public void testParseArgumentsForLocateWithDefaultLimit() throws Exception {
        Parameters parameters = parser.parseArguments(new String[]{"-locate", "blueprint"});
        assertNotNull(parameters);
        assertEquals(10, parameters.getLimit());
        assertFalse(parameters.isOptimize());
        assertTrue(parameters.isLocate());
        assertEquals("blueprint", parameters.getSearchString());
    }

    @Test
    public void testParseArgumentsForOptimize() throws Exception {
        Parameters parameters = parser.parseArguments(new String[]{"-optimize", "blueprint"});
        assertNotNull(parameters);
        assertEquals(10, parameters.getLimit());
        assertTrue(parameters.isOptimize());
        assertFalse(parameters.isLocate());
        assertEquals("blueprint", parameters.getBlueprintName());
    }

    @Test
    public void testDebugFlags() throws ParseException {
        Parameters parameters = parser.parseArguments(new String[]{"-debug"});
        assertTrue(parameters.isDebug());
        assertFalse(parameters.isVerbose());
    }

    @Test
    public void testVerboseFlags() throws ParseException {
        Parameters parameters = parser.parseArguments(new String[]{"-verbose"});
        assertFalse(parameters.isDebug());
        assertTrue(parameters.isVerbose());
    }

    @Test
    public void testDebugVerboseFlags() throws ParseException {
        Parameters parameters = parser.parseArguments(new String[]{"-verbose", "-debug"});
        assertTrue(parameters.isDebug());
        assertTrue(parameters.isVerbose());
    }

    @Test
    public void testVersionFlags() throws ParseException {
        Parameters parameters = parser.parseArguments(new String[]{"-version"});
        assertTrue(parameters.isVersion());
    }
}