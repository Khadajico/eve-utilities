/*
 * Copyright (C) 2016 Khadajico
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package com.arrggh.eve.blueprint.model;

import com.arrggh.eve.blueprint.utilities.ObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class EvePriceHistoryTest {
    @Test
    public void testStringToJson() throws IOException {
        ObjectMapper mapper = ObjectMapperFactory.buildObjectMapper();
        EvePriceHistory history = mapper.readValue(EvePriceHistoryTest.class.getResourceAsStream("/test.price"), EvePriceHistory.class);

        assertEquals(152.26, history.findLatestPrice().get(), 0.01);
    }
}