package com.arrggh.eve.blueprint.data;

import org.junit.Test;

import java.io.IOException;

public class TypeLoaderTest {
    @Test
    public void loadTypeFile() throws IOException {
        TypeLoader loader = new TypeLoader();
        loader.loadFile();
    }
}