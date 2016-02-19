package com.arrggh.eve.blueprint.optimizer;

import com.arrggh.eve.blueprint.model.EveMaterial;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
class BuildTreeMaterialNode implements BuildTreeNode {
    private EveMaterial material;
    private double buyPrice;
    private int quantity;

    @Override
    public double getBuildPrice() {
        return buyPrice;
    }
}