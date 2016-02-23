package com.arrggh.eve.blueprint.optimizer.nodes;

import com.arrggh.eve.blueprint.data.TypeLoader;
import com.arrggh.eve.blueprint.model.EveBlueprint;
import com.arrggh.eve.blueprint.model.EveMaterial;
import com.arrggh.eve.blueprint.model.EveType;
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
    private Optional<Double> buildPrice;
    private long quantity;
    private List<BuildTreeNode> children;

    @Override
    public Optional<Boolean> getShouldBuy() {
        if (buyPrice.isPresent() && buildPrice.isPresent()) {
            return Optional.of(buyPrice.get() <= buildPrice.get());
        }
        return Optional.empty();
    }

    @Override
    public void updateTreePrices() {
        buildPrice = Optional.of(0.0);
        buyPrice = Optional.empty();

        for (BuildTreeNode child : children) {
            child.updateTreePrices();
            if (buildPrice.isPresent() && child.getShouldBuy().isPresent()) {
                boolean childShouldBuy = child.getShouldBuy().get();
                if (childShouldBuy && child.getBuyPrice().isPresent()) {
                    buildPrice = Optional.of(buildPrice.get() + child.getBuyPrice().get());
                } else if (!childShouldBuy && child.getBuildPrice().isPresent()) {
                    buildPrice = Optional.of(buildPrice.get() + child.getBuyPrice().get());
                } else {
                    buildPrice = Optional.empty();
                }
            }
        }

        if (unitBuyPrice.isPresent()) {
            buyPrice = Optional.of(unitBuyPrice.get() * quantity);
        }
    }

    @Override
    public void generateBuildBuyLists(TypeLoader typeLoader, Map<EveType, Long> shoppingList, Map<EveType, Long> buildList) {
        EveType type = typeLoader.getType(produces.getTypeId());
        if (getShouldBuy().isPresent() && getShouldBuy().get()) {
            shoppingList.put(type, shoppingList.getOrDefault(type, 0l) + quantity);
        } else {
            buildList.put(type, buildList.getOrDefault(type, 0l) + quantity);
            for (BuildTreeNode child : children) {
                child.generateBuildBuyLists(typeLoader, shoppingList, buildList);
            }
        }
    }
}
