package com.arrggh.eve.blueprint.optimizer.nodes;

import com.arrggh.eve.blueprint.data.TypeLoader;
import com.arrggh.eve.blueprint.model.EveMaterial;
import com.arrggh.eve.blueprint.model.EveType;
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
    private long quantity;

    @Override
    public Optional<Boolean> getShouldBuy() {
        return unitBuyPrice.isPresent() ? Optional.of(true) : Optional.empty();
    }

    @Override
    public Optional<Double> getBuildPrice() {
        // You can never build a material, i.e. No blueprint should exist for this item
        return Optional.empty();
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
    public void generateBuildBuyLists(TypeLoader typeLoader, Map<EveType, Long> shoppingList, Map<EveType, Long> buildList) {
        EveType type = typeLoader.getType(material.getTypeId());
        shoppingList.put(type, shoppingList.getOrDefault(type, 0l) + quantity);
    }
}