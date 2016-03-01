/*
 * Copyright (C) 2016 Khadajico
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package com.arrggh.eve.blueprint.model;

import lombok.Data;

/**
 * CREST Daily Market Price History
 * <p>
 * A list of these POJOs are populated when parsing the JSON object that is returned when
 * a CREST price query is executed.
 */
@Data
public class EvePriceHistoryItem {
    private String volume_str;
    private int orderCount;
    private double lowPrice;
    private double highPrice;
    private double avgPrice;
    private long volume;
    private String orderCount_str;
    private String date;
}
