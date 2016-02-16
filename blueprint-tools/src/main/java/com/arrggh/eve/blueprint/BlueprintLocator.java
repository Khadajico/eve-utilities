package com.arrggh.eve.blueprint;

import com.arrggh.eve.blueprint.data.BlueprintLoader;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.logging.log4j.LogManager.getLogger;

public class BlueprintLocator {
    private static final Logger LOG = getLogger(BlueprintLocator.class);;
    private final BlueprintLoader blueprintLoader;
    private final String searchString;
    private int limit;

    public BlueprintLocator(BlueprintLoader blueprintLoader, String searchString, int limit) {
        this.blueprintLoader = blueprintLoader;
        this.searchString = searchString;
        this.limit = limit;
    }

    public void locate() {
        System.out.print("Searching blueprint list ...");
        Set<String> results = new HashSet<>();
        for (String blueprintName : blueprintLoader.getBlueprintNames()) {
            if (blueprintName.contains(searchString)) {
                results.add(blueprintName);
            }
        }
        System.out.println("done (" + results.size() + " found)");
        if (results.size() > limit) {
            System.out.println("Too many results found (limit " + limit + ") ... refine string and search again");
        } else {
            for (String result : results) {
                System.out.println(" " + result);
            }
        }
    }
}
