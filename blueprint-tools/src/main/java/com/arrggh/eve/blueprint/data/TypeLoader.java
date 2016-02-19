package com.arrggh.eve.blueprint.data;

import com.arrggh.eve.blueprint.model.EveTypeFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

import static org.apache.logging.log4j.LogManager.getLogger;

public class TypeLoader {
    private static final Logger LOG = getLogger(TypeLoader.class);
    private EveTypeFile fileContents;

    public void loadFile() throws IOException {
        System.out.print("Loading types ... ");
        ObjectMapper mapper = new ObjectMapper();
        InputStream typeStream = BlueprintLoader.class.getResourceAsStream("/data/eve-types.json");
        fileContents = mapper.readValue(typeStream, EveTypeFile.class);
        System.out.println(" done (" + fileContents.getTypes().size() + " loaded)");
    }
}
