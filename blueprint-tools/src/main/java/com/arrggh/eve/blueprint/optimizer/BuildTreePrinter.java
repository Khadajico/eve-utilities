package com.arrggh.eve.blueprint.optimizer;

import com.arrggh.eve.blueprint.model.EveBlueprint;
import com.arrggh.eve.blueprint.model.EveMaterial;

import java.io.PrintStream;
import java.util.stream.Collectors;

public class BuildTreePrinter {
    private static final String SPACING = "  ";
    private final PrintStream output;

    public BuildTreePrinter(PrintStream output) {
        this.output = output;
    }

    public void printTree(BuildTreeNode root) {
        output.println("Name                              Buy Price           Build Price         Action");
        output.println("------------------------------    ----------------    ----------------    -------");
        printTreeNode("", (BuildTreeBlueprintNode) root);
    }

    private void printTreeNode(String offset, BuildTreeBlueprintNode node) {
        EveBlueprint blueprint = node.getBlueprint();
        EveMaterial material = blueprint.getManufacture().getProduces().get(0);

        double buildPrice = node.getChildren().stream().collect(Collectors.summingDouble(child -> child.getBuyPrice() * child.getQuantity()));
        double buyPrice = node.getBuyPrice();
        String action = buyPrice < buildPrice ? "Buy" : "Build";
        output.println(String.format("%-30s    %16.2f    %16.2f    %-8s", offset + material.getName() + "(" + material.getQuantity() + ")", buyPrice, buildPrice, action));

        for (BuildTreeNode child : node.getChildren()) {
            if (child instanceof BuildTreeBlueprintNode) {
                printTreeNode(offset + SPACING, (BuildTreeBlueprintNode) child);
            } else if (child instanceof BuildTreeMaterialNode) {
                printTreeNode(offset + SPACING, (BuildTreeMaterialNode) child);
            } else {
                throw new IllegalArgumentException("Unknown Build Tree Node Type '" + child.getClass());
            }
        }
    }

    private void printTreeNode(String offset, BuildTreeMaterialNode node) {
        EveMaterial material = node.getMaterial();
        double buyPrice = material.getQuantity() * node.getBuyPrice();
        output.println(String.format("%-30s    %16s    %16.2f    %-8s", offset + material.getName() + "(" + material.getQuantity() + ")", "", buyPrice, "Buy"));
    }
}
