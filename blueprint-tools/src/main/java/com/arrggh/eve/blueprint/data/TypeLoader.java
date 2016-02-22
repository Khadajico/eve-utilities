package com.arrggh.eve.blueprint.data;

import com.arrggh.eve.blueprint.model.EveType;
import com.arrggh.eve.blueprint.model.EveTypeFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.apache.logging.log4j.LogManager.getLogger;

public class TypeLoader {
    private static final Logger LOG = getLogger(TypeLoader.class);
    private Map<Integer, EveType> cachedTypes = new HashMap<>();

    public void loadFile() throws IOException {
        System.out.print("Loading types ... ");
        ObjectMapper mapper = new ObjectMapper();
        InputStream typeStream = BlueprintLoader.class.getResourceAsStream("/data/eve-types.json");
        EveTypeFile fileContents = mapper.readValue(typeStream, EveTypeFile.class);
        System.out.println(" done (" + fileContents.getTypes().size() + " loaded)");
        for (EveType eveType : fileContents.getTypes()) {
            cachedTypes.put(eveType.getId(), eveType);
        }
    }

    public EveType getType(int typeId) {
        return cachedTypes.get(typeId);
    }
}
