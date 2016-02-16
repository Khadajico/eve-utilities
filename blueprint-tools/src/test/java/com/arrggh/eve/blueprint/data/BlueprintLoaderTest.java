package com.arrggh.eve.blueprint.data;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class BlueprintLoaderTest {
    @Test
    public void loadTypeFile() throws IOException {
        BlueprintLoader loader = new BlueprintLoader();
        loader.loadFile();
    }
}