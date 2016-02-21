package com.arrggh.eve.blueprint.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class EvePriceHistoryTest {
    @Test
    public void testStringToJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        EvePriceHistory history = mapper.readValue(EvePriceHistoryTest.class.getResourceAsStream("/test.price"), EvePriceHistory.class);

        assertEquals(152.26, history.findLatestPrice(), 0.01);
    }
}