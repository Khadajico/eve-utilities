package com.arrggh.eve.blueprint.data;

import com.arrggh.eve.blueprint.model.EveBlueprint;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BlueprintLoaderTest {

    private static BlueprintLoader blueprintLoader;

    @BeforeClass
    public static void createTestFramework() {
        blueprintLoader = new BlueprintLoader();

        blueprintLoader.addBlueprint(EveBlueprint.builder().id(100).marketGroupId(1).name("Blueprint 1").build());
        blueprintLoader.addBlueprint(EveBlueprint.builder().id(100).marketGroupId(1).name("Blueprint 2").build());
        blueprintLoader.addBlueprint(EveBlueprint.builder().id(100).marketGroupId(1).name("Blueprint 3").build());
        blueprintLoader.addBlueprint(EveBlueprint.builder().id(100).marketGroupId(1).name("Blueprint 4").build());
    }

    @Test
    public void testNoBlueprintsFound() {
        List<String> results = blueprintLoader.matchBlueprintNames("Greenprint", 2);
        assertEquals(0, results.size());
    }

    @Test
    public void testOneBlueprintsFound() {
        List<String> results = blueprintLoader.matchBlueprintNames("3", 2);
        assertEquals(1, results.size());
        assertEquals("Blueprint 3", results.get(0));
    }

    @Test
    public void testTooManyuBlueprintsFound() {
        List<String> results = blueprintLoader.matchBlueprintNames("Blue", 2);
        assertEquals(2, results.size());
    }

    @Test
    public void testDataFileLoadsSuccessfully() throws IOException {
        BlueprintLoader loader = new BlueprintLoader();
        loader.loadFile();
        assertEquals(2915, loader.getBlueprintNames().size());
    }
}