package com.arrggh.eve.blueprint.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketPrice {
    private long time;
    private int typeId;
    private int marketId;
    private double price;
}
