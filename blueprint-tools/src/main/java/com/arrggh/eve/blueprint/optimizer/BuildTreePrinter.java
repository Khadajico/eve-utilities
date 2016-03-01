/*
 * Copyright (C) 2016 Khadajico
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package com.arrggh.eve.blueprint.optimizer;

import com.arrggh.eve.blueprint.model.EveBlueprint;
import com.arrggh.eve.blueprint.model.EveMaterial;
import com.arrggh.eve.blueprint.optimizer.nodes.BuildTreeBlueprintNode;
import com.arrggh.eve.blueprint.optimizer.nodes.BuildTreeMaterialNode;
import com.arrggh.eve.blueprint.optimizer.nodes.BuildTreeNode;
import com.arrggh.eve.blueprint.utilities.ColumnOutputPrinter;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.arrggh.eve.blueprint.utilities.ColumnOutputPrinter.Justification;
import static com.arrggh.eve.blueprint.utilities.ColumnOutputPrinter.Justification.LEFT;
import static com.arrggh.eve.blueprint.utilities.ColumnOutputPrinter.Justification.RIGHT;

public class BuildTreePrinter {
    private static final String NO_DATA = "???";

    private final DecimalFormat doubleFormat;
    private final DecimalFormat longFormat;

    private static final String SPACING = "  ";

    public BuildTreePrinter() {
        doubleFormat = new DecimalFormat("#.00");
        doubleFormat.setGroupingUsed(true);
        doubleFormat.setGroupingSize(3);

        longFormat = new DecimalFormat("#");
        longFormat.setGroupingUsed(true);
        longFormat.setGroupingSize(3);
    }

    public void printTree(BuildTreeNode root) {
        List<String[]> data = new LinkedList<>();

        String[] headers = new String[]{"Name", "Quantity", "Buy Price", "Build Price", "Action", "Profit", "Percentage"};
        Justification[] justifications = new Justification[]{LEFT, RIGHT, RIGHT, RIGHT, LEFT, RIGHT, RIGHT};

        printTreeNode(data, "", (BuildTreeBlueprintNode) root, false);

        ColumnOutputPrinter printer = new ColumnOutputPrinter(headers, justifications, data);
        printer.output();
    }

    private void printTreeNode(List<String[]> data, String offset, BuildTreeBlueprintNode node, boolean obscure) {
        EveBlueprint blueprint = node.getBlueprint();
        EveMaterial material = blueprint.getManufacture().getProduces().get(0);

        String action = obscure ? "*****" : node.getShouldBuy().isPresent() ? node.getShouldBuy().get() ? "Buy" : "Build" : NO_DATA;
        String profit = NO_DATA;
        String percentage = NO_DATA;
        if (node.getBuyPrice().isPresent() && node.getBuildPrice().isPresent()) {
            double profitValue = node.getBuyPrice().get() - node.getBuildPrice().get();
            profit = doubleToString(profitValue);
            percentage = doubleToString(100 * profitValue / node.getBuyPrice().get()) + "%";
        }

        data.add(new String[]{offset + material.getName(),
                longToString(node.getQuantity()),
                doubleToString(node.getBuyPrice()),
                doubleToString(node.getBuildPrice()),
                action, profit, percentage});

        for (BuildTreeNode child : node.getChildren()) {
            if (child instanceof BuildTreeBlueprintNode) {
                printTreeNode(data, offset + SPACING, (BuildTreeBlueprintNode) child, obscure || (node.getShouldBuy().isPresent() && node.getShouldBuy().get()));
            } else if (child instanceof BuildTreeMaterialNode) {
                printTreeNode(data, offset + SPACING, (BuildTreeMaterialNode) child, obscure || (node.getShouldBuy().isPresent() && node.getShouldBuy().get()));
            } else {
                throw new IllegalArgumentException("Unknown Build Tree Node Type '" + child.getClass());
            }
        }
    }

    private void printTreeNode(List<String[]> data, String offset, BuildTreeMaterialNode node, boolean obscure) {
        String action = obscure ? " * " : "Buy";
        EveMaterial material = node.getMaterial();
        data.add(new String[]{offset + material.getName(), longToString(node.getQuantity()), doubleToString(node.getBuyPrice()), "", action, "", ""});
    }

    private String longToString(double value) {
        return longFormat.format(value);
    }

    private String doubleToString(double value) {
        return doubleFormat.format(value);
    }

    private String doubleToString(Optional<Double> value) {
        if (value.isPresent())
            return doubleFormat.format(value.get());
        return NO_DATA;
    }
}
