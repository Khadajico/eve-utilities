package com.arrggh.eve.blueprint.data;

import com.arrggh.eve.blueprint.model.MarketPrice;

public interface MarketPriceCache {
    MarketPrice getPrice(int marketId, int typeId);

    void addPrice(int marketId, int typeId, MarketPrice entry);
}
