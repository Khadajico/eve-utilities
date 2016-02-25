package com.arrggh.eve.blueprint.optimizer;

import com.arrggh.eve.blueprint.data.BlueprintLoader;
import com.arrggh.eve.blueprint.data.PriceQuery;
import com.arrggh.eve.blueprint.data.TypeLoader;
import com.arrggh.eve.blueprint.model.EveBlueprint;
import com.arrggh.eve.blueprint.model.EveMaterial;
import com.arrggh.eve.blueprint.model.EveType;
import com.arrggh.eve.blueprint.optimizer.nodes.BuildTreeBlueprintNode;
import com.arrggh.eve.blueprint.optimizer.nodes.BuildTreeMaterialNode;
import com.arrggh.eve.blueprint.optimizer.nodes.BuildTreeNode;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static org.apache.logging.log4j.LogManager.getLogger;

public class BlueprintOptimizerAction {
    private static final Logger LOG = getLogger(BlueprintOptimizerAction.class);
    private final EveBlueprint blueprint;
    private final TypeLoader typeLoader;
    private final BlueprintLoader blueprintLoader;
    private final PriceQuery priceQuery;

    public BlueprintOptimizerAction(TypeLoader typeLoader, BlueprintLoader blueprintLoader, PriceQuery priceQuery, String blueprintName) {
        this.typeLoader = typeLoader;
        this.blueprintLoader = blueprintLoader;
        this.priceQuery = priceQuery;

        Optional<EveBlueprint> blueprintOptional = blueprintLoader.getBlueprint(blueprintName);
        if (!blueprintOptional.isPresent()) {
            throw new IllegalArgumentException("Cannot find a blueprint called '" + blueprintName + "'");
        }
        blueprint = blueprintOptional.get();
    }

    public BuildManifest generateBuildTree() {
        System.out.println("Starting optimization of blueprint '" + blueprint.getName() + "'");
        BuildTreeBlueprintNode tree = generateBuildTree(1, blueprint);
        tree.updateTreePrices();

        Map<EveType, Long> shoppingList = new HashMap<>();
        Map<EveType, Long> buildList = new HashMap<>();
        tree.generateBuildBuyLists(typeLoader, shoppingList, buildList);

        return BuildManifest.builder().tree(tree).shoppingList(shoppingList).buildList(buildList).build();
    }

    private BuildTreeBlueprintNode generateBuildTree(long required, EveBlueprint bp) {
        EveMaterial produces = bp.getManufacture().getProduces().get(0);

        BuildTreeBlueprintNode blueprintNode = BuildTreeBlueprintNode.builder().blueprint(bp).produces(produces).unitBuyPrice(priceQuery.queryPrice(produces.getTypeId())).quantity(required).build();

        List<BuildTreeNode> children = new LinkedList<>();
        for (EveMaterial material : bp.getManufacture().getMaterials()) {
            Optional<EveBlueprint> blueprintOptional = blueprintLoader.getBlueprintForId(material.getTypeId());
            if (blueprintOptional.isPresent()) {
                long runsNeeded = Math.round(Math.ceil(1.0 * required * material.getQuantity() / produces.getQuantity()));
                children.add(generateBuildTree(runsNeeded, blueprintOptional.get()));
            } else {
                BuildTreeMaterialNode materialNode = BuildTreeMaterialNode.builder().material(material).unitBuyPrice(priceQuery.queryPrice(material.getTypeId())).quantity(material.getQuantity() * required).build();
                children.add(materialNode);
            }
        }
        blueprintNode.setChildren(children);
        return blueprintNode;
    }
}
