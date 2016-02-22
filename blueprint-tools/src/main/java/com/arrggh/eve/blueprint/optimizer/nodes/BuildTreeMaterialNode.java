package com.arrggh.eve.blueprint.optimizer.nodes;

import com.arrggh.eve.blueprint.model.EveMaterial;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Optional;

@Data
@Builder
public class BuildTreeMaterialNode implements BuildTreeNode {
    private EveMaterial material;
    private Optional<Double> buyPrice;
    private Optional<Double> unitBuyPrice;
    private double buildPrice;
    private long quantity;

    @Override
    public boolean getShouldBuy() {
        return true;
    }

    @Override
    public void updateTreePrices() {
        if (unitBuyPrice.isPresent()) {
            buyPrice = Optional.of(unitBuyPrice.get() * quantity);
        } else {
            buyPrice = Optional.empty();
        }
    }

    @Override
    public void generateBuildBuyLists(Map<Integer, Long> shoppingList, Map<Integer, Long> buildList) {
        shoppingList.put(material.getTypeId(), shoppingList.getOrDefault(material.getTypeId(), 0l) + quantity);
    }
}