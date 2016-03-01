/*
 * Copyright (C) 2016 Khadajico
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
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