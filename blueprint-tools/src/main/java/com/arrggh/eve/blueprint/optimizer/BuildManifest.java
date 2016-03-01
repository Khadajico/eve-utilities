/*
 * Copyright (C) 2016 Khadajico
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package com.arrggh.eve.blueprint.optimizer;

import com.arrggh.eve.blueprint.model.EveType;
import com.arrggh.eve.blueprint.optimizer.nodes.BuildTreeNode;
import com.arrggh.eve.blueprint.utilities.ColumnOutputPrinter;
import lombok.Builder;
import lombok.Value;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.arrggh.eve.blueprint.utilities.ColumnOutputPrinter.Justification;
import static com.arrggh.eve.blueprint.utilities.ColumnOutputPrinter.Justification.LEFT;
import static com.arrggh.eve.blueprint.utilities.ColumnOutputPrinter.Justification.RIGHT;

@Value
@Builder
public class BuildManifest {
    private Map<EveType, Long> shoppingList;
    private Map<EveType, Long> buildList;
    private BuildTreeNode tree;

    private static final DecimalFormat decimalFormat = buildDecimalFormatter();

    private static DecimalFormat buildDecimalFormatter() {
        DecimalFormat decimalFormat = new DecimalFormat("#");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        return decimalFormat;
    }

    private static final Comparator<String[]> LIST_COMPARATOR = new Comparator<String[]>() {
        @Override
        public int compare(String[] lhs, String[] rhs) {
            try {
                int valueCompare = Long.compare(decimalFormat.parse(rhs[1]).longValue(), decimalFormat.parse(lhs[1]).longValue());
                if (valueCompare == 0)
                    return lhs[0].compareTo(rhs[0]);
                return valueCompare;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }
    };

    public String dumpToConsole() {
        String[] buyHeaders = {"Buy", "Quantity"};
        String[] buildHeaders = {"Build", "Quantity"};
        Justification[] justifications = new Justification[]{LEFT, RIGHT};

        List<String[]> buyData = new LinkedList<>();
        for (Map.Entry<EveType, Long> entries : shoppingList.entrySet()) {
            buyData.add(new String[]{entries.getKey().getName(), decimalFormat.format(entries.getValue())});
        }

        List<String[]> buildData = new LinkedList<>();
        for (Map.Entry<EveType, Long> entries : buildList.entrySet()) {
            buildData.add(new String[]{entries.getKey().getName(), decimalFormat.format(entries.getValue())});
        }

        buyData.sort(LIST_COMPARATOR);
        buildData.sort(LIST_COMPARATOR);

        BuildTreePrinter printer = new BuildTreePrinter();
        printer.printTree(tree);
        System.out.println("");
        ColumnOutputPrinter buyPrinter = new ColumnOutputPrinter(buyHeaders, justifications, buyData);
        buyPrinter.output();
        System.out.println("");

        ColumnOutputPrinter buildPrinter = new ColumnOutputPrinter(buildHeaders, justifications, buildData);
        buildPrinter.output();
        System.out.println("");
        return "";
    }
}
