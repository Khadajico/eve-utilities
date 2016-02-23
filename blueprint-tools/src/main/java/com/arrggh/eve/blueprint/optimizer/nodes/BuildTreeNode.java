package com.arrggh.eve.blueprint.optimizer.nodes;

import com.arrggh.eve.blueprint.data.TypeLoader;
import com.arrggh.eve.blueprint.model.EveType;

import java.util.Map;
import java.util.Optional;

public interface BuildTreeNode {
    Optional<Boolean> getShouldBuy();

    Optional<Double> getBuyPrice();

    Optional<Double> getBuildPrice();

    void updateTreePrices();

    void generateBuildBuyLists(TypeLoader typeLoader, Map<EveType, Long> shoppingList, Map<EveType, Long> buildList);
}


