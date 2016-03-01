/*
 * Copyright (C) 2016 Khadajico
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package com.arrggh.eve.blueprint.data;

import com.arrggh.eve.blueprint.model.EveBlueprint;
import com.arrggh.eve.blueprint.model.EveBlueprintFile;
import com.arrggh.eve.blueprint.utilities.ObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.LogManager.getLogger;

public class BlueprintLoader {
    private static final Logger LOG = getLogger(BlueprintLoader.class);

    private Map<String, EveBlueprint> blueprints = new HashMap<>();
    private Map<Integer, String> typeIdToBlueprintName = new HashMap<>();

    public BlueprintLoader() {
    }

    public void loadFile() throws IOException {
        System.out.print("Loading blueprints ... ");
        ObjectMapper mapper = ObjectMapperFactory.buildObjectMapper();
        InputStream typeStream = BlueprintLoader.class.getResourceAsStream("/data/eve-blueprints.json");
        EveBlueprintFile fileContents = mapper.readValue(typeStream, EveBlueprintFile.class);
        for (EveBlueprint blueprint : fileContents.getBlueprints()) {
            addBlueprint(blueprint);
        }
        System.out.println(" done (" + blueprints.size() + " loaded)");
    }

    public void addBlueprint(EveBlueprint blueprint) {
        blueprints.put(blueprint.getName(), blueprint);
        if (blueprint.getManufacture() != null) {
            typeIdToBlueprintName.put(blueprint.getManufacture().getProduces().get(0).getTypeId(), blueprint.getName());
        }
    }

    public Set<String> getBlueprintNames() {
        return blueprints.keySet();
    }

    public Optional<EveBlueprint> getBlueprint(String name) {
        return Optional.ofNullable(blueprints.get(name));
    }

    public Optional<EveBlueprint> getBlueprintForId(int typeId) {
        if (!typeIdToBlueprintName.containsKey(typeId)) {
            return Optional.empty();
        }
        return Optional.of(blueprints.get(typeIdToBlueprintName.get(typeId)));
    }

    /**
     * Search all the blueprints that the BlueprintLoader has in memory
     * looking for the first <i>limit</i> results. The results are
     * returned in alphabetic order.#
     *
     * @return The sorted list of matching blueprint names
     */
    public List<String> matchBlueprintNames(String searchString, int limit) {
        System.out.print("Searching blueprint list ...");
        Set<String> results = blueprints.keySet().stream().filter(blueprintName -> blueprintName.contains(searchString)).limit(limit).collect(Collectors.toSet());
        List<String> sortedResults = new ArrayList<>(results.size());
        sortedResults.addAll(results);
        sortedResults.sort((lhs, rhs) -> lhs.compareTo(rhs));
        System.out.println("done (" + sortedResults.size() + " found)");
        return sortedResults;
    }
}
