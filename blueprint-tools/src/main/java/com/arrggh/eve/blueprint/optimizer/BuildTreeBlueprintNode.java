package com.arrggh.eve.blueprint.optimizer;

import com.arrggh.eve.blueprint.model.EveBlueprint;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
class BuildTreeBlueprintNode implements BuildTreeNode {
    private EveBlueprint blueprint;
    private double buyPrice;
    private double buildPrice;
    private int quantity;
    private List<BuildTreeNode> children;
    private boolean shouldBuy;

    public void setChildren(List<BuildTreeNode> children) {
        this.children = children;
        for (BuildTreeNode node : children) {
            buildPrice += node.getBuildPrice();
        }
        shouldBuy = buyPrice < buildPrice;
    }
}
