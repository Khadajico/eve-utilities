package com.arrggh.eve.blueprint.optimizer.nodes;

import java.util.Map;
import java.util.Optional;

public interface BuildTreeNode {
    boolean getShouldBuy();

    Optional<Double> getBuyPrice();

    double getBuildPrice();

    void updateTreePrices();

    void generateBuildBuyLists(Map<Integer, Long> shoppingList, Map<Integer, Long> buildList);
}


