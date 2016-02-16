package com.arrggh.eve.blueprint.data;

import com.arrggh.eve.blueprint.model.EveBlueprint;
import com.arrggh.eve.blueprint.model.EveBlueprintFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.LogManager.getLogger;

public class BlueprintLoader {
    private static final Logger LOG = getLogger(BlueprintLoader.class);
    ;

    private Map<String, EveBlueprint> blueprints = new HashMap<>();
    private Map<Integer, String> typeIdToBlueprintName = new HashMap<>();

    public BlueprintLoader() {
    }

    public void loadFile() throws IOException {
        System.out.print("Loading blueprints ... ");
        ObjectMapper mapper = new ObjectMapper();
        InputStream typeStream = BlueprintLoader.class.getResourceAsStream("/eve-blueprints.json");
        EveBlueprintFile fileContents = mapper.readValue(typeStream, EveBlueprintFile.class);
        for (EveBlueprint blueprint : fileContents.getBlueprints()) {
            blueprints.put(blueprint.getName(), blueprint);
            if (blueprint.getManufacture() != null) {
                typeIdToBlueprintName.put(blueprint.getManufacture().getProduces().get(0).getTypeId(), blueprint.getName());
            }
        }
        System.out.println(" done (" + blueprints.size() + " loaded)");
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
}
