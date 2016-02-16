package com.arrggh.eve.blueprint.data;

import java.util.Random;

public class PriceQuery {
    private static final Random random = new Random(System.nanoTime());

    public float queryPrice(int typeId) {
        return random.nextFloat();
    }
}
