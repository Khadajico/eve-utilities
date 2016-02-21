package com.arrggh.eve.blueprint.model;

import lombok.Data;

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
