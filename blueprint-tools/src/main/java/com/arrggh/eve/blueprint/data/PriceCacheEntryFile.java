package com.arrggh.eve.blueprint.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceCacheEntryFile {
    private List<PriceCacheEntry> prices;
}
