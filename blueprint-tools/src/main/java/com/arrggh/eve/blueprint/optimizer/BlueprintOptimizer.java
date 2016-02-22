package com.arrggh.eve.blueprint.optimizer;

import com.arrggh.eve.blueprint.data.BlueprintLoader;
import com.arrggh.eve.blueprint.data.PriceQuery;
import com.arrggh.eve.blueprint.data.TypeLoader;
import com.arrggh.eve.blueprint.model.EveBlueprint;
import com.arrggh.eve.blueprint.model.EveMaterial;
import com.arrggh.eve.blueprint.optimizer.nodes.BuildTreeBlueprintNode;
import com.arrggh.eve.blueprint.optimizer.nodes.BuildTreeMaterialNode;
import com.arrggh.eve.blueprint.optimizer.nodes.BuildTreeNode;
import com.arrggh.eve.blueprint.utilities.ColumnOutputPrinter;
import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;
import java.util.*;

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
        tree.updateTreePrices();

        BuildTreePrinter printer = new BuildTreePrinter(System.out);
        printer.printTree(tree);

        Map<Integer, Long> shoppingList = new HashMap<>();
        Map<Integer, Long> buildList = new HashMap<>();
        tree.generateBuildBuyLists(shoppingList, buildList);

        String[] buyHeaders = {"Buy", "Quantity"};
        String[] buildHeaders = {"Build", "Quantity"};
        Class<?>[] classes = new Class<?>[]{String.class, String.class};

        DecimalFormat decimalFormat = new DecimalFormat("#");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);

        List<String[]> buyData = new LinkedList<>();
        for (Map.Entry<Integer, Long> entries : shoppingList.entrySet()) {
            buyData.add(new String[]{typeLoader.getType(entries.getKey()).getName(), decimalFormat.format(entries.getValue())});
        }

        List<String[]> buildData = new LinkedList<>();
        for (Map.Entry<Integer, Long> entries : buildList.entrySet()) {
            buildData.add(new String[]{typeLoader.getType(entries.getKey()).getName(), decimalFormat.format(entries.getValue())});
        }

        ColumnOutputPrinter buyPrinter = new ColumnOutputPrinter(buyHeaders, classes, buyData);
        ColumnOutputPrinter buildPrinter = new ColumnOutputPrinter(buyHeaders, classes, buildData);

        buyPrinter.output();
        buildPrinter.output();
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
