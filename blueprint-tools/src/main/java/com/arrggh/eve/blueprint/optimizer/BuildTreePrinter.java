package com.arrggh.eve.blueprint.optimizer;

import com.arrggh.eve.blueprint.model.EveBlueprint;
import com.arrggh.eve.blueprint.model.EveMaterial;
import com.arrggh.eve.blueprint.optimizer.nodes.BuildTreeBlueprintNode;
import com.arrggh.eve.blueprint.optimizer.nodes.BuildTreeMaterialNode;
import com.arrggh.eve.blueprint.optimizer.nodes.BuildTreeNode;
import com.arrggh.eve.blueprint.utilities.ColumnOutputPrinter;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class BuildTreePrinter {
    private final DecimalFormat doubleFormat;
    private final DecimalFormat longFormat;

    private static final String SPACING = "  ";
    private final PrintStream output;

    public BuildTreePrinter(PrintStream output) {
        doubleFormat = new DecimalFormat("#.00");
        doubleFormat.setGroupingUsed(true);
        doubleFormat.setGroupingSize(3);

        longFormat = new DecimalFormat("#");
        longFormat.setGroupingUsed(true);
        longFormat.setGroupingSize(3);

        this.output = output;
    }

    public void printTree(BuildTreeNode root) {
        List<String[]> data = new LinkedList<>();

        String[] headers = new String[]{"Name", "Quantity", "Unit Price", "Buy Price", "Build Price", "Action", "Profit", "Percentage"};
        Class<?>[] classes = new Class<?>[]{String.class, Number.class, Number.class, Number.class, Number.class, String.class, Number.class, Number.class};

        printTreeNode(data, "", (BuildTreeBlueprintNode) root, false);

        ColumnOutputPrinter printer = new ColumnOutputPrinter(headers, classes, data);
        printer.output();
    }

    private void printTreeNode(List<String[]> data, String offset, BuildTreeBlueprintNode node, boolean obscure) {
        EveBlueprint blueprint = node.getBlueprint();
        EveMaterial material = blueprint.getManufacture().getProduces().get(0);

        String action = obscure ? "*****" : node.getShouldBuy() ? "Buy" : "Build";
        double profit = 0.0;
        double percentage = 0.0;
        if (node.getBuyPrice().isPresent()) {
            profit = node.getBuyPrice().get() - node.getBuildPrice();
            percentage = 100 * profit / node.getBuyPrice().get();
        }

        data.add(new String[]{offset + material.getName(),
                longToString(node.getQuantity()),
                doubleToString(node.getUnitBuyPrice()),
                doubleToString(node.getBuyPrice()),
                doubleToString(node.getBuildPrice()),
                action, doubleToString(profit), doubleToString(percentage)});

        for (BuildTreeNode child : node.getChildren()) {
            if (child instanceof BuildTreeBlueprintNode) {
                printTreeNode(data, offset + SPACING, (BuildTreeBlueprintNode) child, obscure || node.getShouldBuy());
            } else if (child instanceof BuildTreeMaterialNode) {
                printTreeNode(data, offset + SPACING, (BuildTreeMaterialNode) child, obscure || node.getShouldBuy());
            } else {
                throw new IllegalArgumentException("Unknown Build Tree Node Type '" + child.getClass());
            }
        }
    }

    private void printTreeNode(List<String[]> data, String offset, BuildTreeMaterialNode node, boolean obscure) {
        String action = obscure ? "*****" : "Buy";
        EveMaterial material = node.getMaterial();
        data.add(new String[]{offset + material.getName(), longToString(node.getQuantity()), doubleToString(node.getUnitBuyPrice()), doubleToString(node.getBuyPrice()), "", action, "", ""});
    }

    private String longToString(double value) {
        return String.format("%16s", longFormat.format(value));
    }

    private String doubleToString(double value) {
        return String.format("%16s", doubleFormat.format(value));
    }

    private String doubleToString(Optional<Double> value) {
        if (value.isPresent())
            return String.format("%16s", doubleFormat.format(value.get()));
        return "---<no data>---";
    }
}
