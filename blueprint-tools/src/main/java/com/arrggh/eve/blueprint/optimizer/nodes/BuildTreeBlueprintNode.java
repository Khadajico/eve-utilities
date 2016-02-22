package com.arrggh.eve.blueprint.optimizer.nodes;

import com.arrggh.eve.blueprint.model.EveBlueprint;
import com.arrggh.eve.blueprint.model.EveMaterial;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
@Builder
public class BuildTreeBlueprintNode implements BuildTreeNode {
    private EveBlueprint blueprint;
    private EveMaterial produces;
    private Optional<Double> buyPrice;
    private Optional<Double> unitBuyPrice;
    private double buildPrice;
    private long quantity;
    private List<BuildTreeNode> children;

    @Override
    public boolean getShouldBuy() {
        if (buyPrice.isPresent()) {
            return buyPrice.get() <= buildPrice;
        }
        return false;
    }

    @Override
    public void updateTreePrices() {
        buildPrice = 0.0;
        buyPrice = Optional.empty();

        for (BuildTreeNode child : children) {
            child.updateTreePrices();
            buildPrice += child.getShouldBuy() ? child.getBuyPrice().get() : child.getBuildPrice();
        }

        if (unitBuyPrice.isPresent()) {
            buyPrice = Optional.of(unitBuyPrice.get() * quantity);
        }
    }

    @Override
    public void generateBuildBuyLists(Map<Integer, Long> shoppingList, Map<Integer, Long> buildList) {
        if (getShouldBuy()) {
            shoppingList.put(produces.getTypeId(), shoppingList.getOrDefault(produces.getTypeId(), 0l) + quantity);
        } else {
            buildList.put(produces.getTypeId(), buildList.getOrDefault(produces.getTypeId(), 0l) + quantity);
            for (BuildTreeNode child : children) {
                child.generateBuildBuyLists(shoppingList, buildList);
            }
        }
    }
}
