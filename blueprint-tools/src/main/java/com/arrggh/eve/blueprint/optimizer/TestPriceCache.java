package com.arrggh.eve.blueprint.optimizer;

import com.arrggh.eve.blueprint.data.MarketPriceCache;
import com.arrggh.eve.blueprint.model.MarketPrice;

public class TestPriceCache implements MarketPriceCache {
    @Override
    public MarketPrice getPrice(int marketId, int typeId) {
        return MarketPrice.builder().time(System.nanoTime()).price(lookup(typeId)).build();
    }

    private double lookup(int typeId) {
        switch (typeId) {
            case 21638: // Vespa II
                return 0.0f;
            case 9834: // Guidance Systems
                return 1.0f;
            case 11399: // Morphite
                return 2.0f;
            case 15508: // Vespa I
                return 3.0f;
            case 34: // Tritanium
                return 4.0f;
            case 35: // Pyerite
                return 5.0f;
            case 36: // Mexallon
                return 6.0f;
            case 37: // Isogen
                return 7.0f;
            case 38: // Nocxium
                return 8.0f;
            case 39: // Zydrine
                return 9.0f;
            case 9848: // Robotics
                return 10.0f;
            case 11481: // R.A.M.- Robotics
                return 40.0f;
            case 11690: // Superconductor Rails
                return 12.0f;
            case 16682: // Hypersynaptic Fibers
                return 13.0f;
            case 16679: // Fullerides
                return 14.0f;
            case 16671: // Titanium Carbide
                return 15.0f;
        }
        return -1.0f;
    }

    @Override
    public void addPrice(int marketId, int typeId, MarketPrice entry) {
        throw new UnsupportedOperationException("Cannot do this yet");
    }
}
