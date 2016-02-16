package com.arrggh.eve.blueprint.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceCacheEntry {
    private int typeId;
    private int marketId;
    private float price;
}
