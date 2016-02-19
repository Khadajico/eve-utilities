package com.arrggh.eve.blueprint.optimizer;

import com.arrggh.eve.blueprint.data.BlueprintLoader;
import com.arrggh.eve.blueprint.data.PriceQuery;
import com.arrggh.eve.blueprint.data.TypeLoader;
import com.arrggh.eve.blueprint.model.EveBlueprint;
import com.arrggh.eve.blueprint.model.EveMaterial;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.apache.logging.log4j.LogManager.getLogger;

public class BlueprintOptimizer {
    private static final Logger LOG = getLogger(BlueprintOptimizer.class);
    private final EveBlueprint blueprint;
    private final TypeLoader typeLoader;
    private final BlueprintLoader blueprintLoader;
    private final PriceQuery priceQuery;

    public BlueprintOptimizer(TypeLoader typeLoader, BlueprintLoader blueprintLoader, PriceQuery priceQuery, String blueprintName) {
        this.typeLoader = typeLoader;
        this.blueprintLoader = blueprintLoader;
        this.priceQuery = priceQuery;

        Optional<EveBlueprint> blueprintOptional = blueprintLoader.getBlueprint(blueprintName);
        if (!blueprintOptional.isPresent()) {
            throw new IllegalArgumentException("Cannot find a blueprint called '" + blueprintName + "'");
        }
        blueprint = blueprintOptional.get();
    }

    public void generateBuildTree() {
        System.out.println("Starting optimization of blueprint '" + blueprint.getName() + "'");

        BuildTreeBlueprintNode tree = generateBuildTree(1, blueprint);
        BuildTreePrinter printer = new BuildTreePrinter(System.out);
        printer.printTree(tree);
    }

    private BuildTreeBlueprintNode generateBuildTree(long required, EveBlueprint bp) {
        EveMaterial produces = bp.getManufacture().getProduces().get(0);

        BuildTreeBlueprintNode blueprintNode = BuildTreeBlueprintNode.builder().blueprint(bp).buyPrice(priceQuery.queryPrice(produces.getTypeId())).quantity(produces.getQuantity()).build();

        List<BuildTreeNode> children = new LinkedList<>();
        for (EveMaterial material : bp.getManufacture().getMaterials()) {
            Optional<EveBlueprint> blueprintOptional = blueprintLoader.getBlueprintForId(material.getTypeId());
            if (blueprintOptional.isPresent()) {
                long runsNeeded = Math.round(Math.ceil(1.0 * material.getQuantity() / produces.getQuantity()));
                System.err.println("We need " + required + " blueprint makes " + produces.getQuantity() + " per run, so need " + runsNeeded + " runs");
                children.add(generateBuildTree(1, blueprintOptional.get()));
            } else {
                BuildTreeMaterialNode materialNode = BuildTreeMaterialNode.builder().material(material).buyPrice(priceQuery.queryPrice(material.getTypeId())).quantity(material.getQuantity()).build();
                children.add(materialNode);
            }
        }
        blueprintNode.setChildren(children);
        return blueprintNode;
    }
}
