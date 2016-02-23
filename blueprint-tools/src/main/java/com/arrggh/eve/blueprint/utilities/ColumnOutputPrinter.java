package com.arrggh.eve.blueprint.utilities;

import java.util.List;

public class ColumnOutputPrinter {
    public enum Justification {
        LEFT, RIGHT;
    }

    private final String[] headers;
    private final Justification[] justifications;
    private final List<String[]> data;

    public ColumnOutputPrinter(String[] headers, Justification[] justifications, List<String[]> data) {
        this.headers = headers;
        this.justifications = justifications;
        this.data = data;
    }

    public void output() {
        int widths[] = new int[headers.length];
        for (int c = 0; c != widths.length; c++) {
            widths[c] = headers[c].length();
        }

        for (String[] row : data) {
            for (int c = 0; c != widths.length; c++) {
                if (widths[c] < row[c].length()) {
                    widths[c] = row[c].length();
                }
            }
        }

        StringBuilder headerStringBuilder = new StringBuilder();
        StringBuilder formatStringBuilder = new StringBuilder();
        for (int c = 0; c != widths.length; c++) {
            headerStringBuilder.append("%-" + widths[c] + "s    ");
            if (justifications[c] == Justification.LEFT) {
                formatStringBuilder.append("%-" + widths[c] + "s    ");
            } else if (justifications[c] == Justification.RIGHT) {
                formatStringBuilder.append("%" + widths[c] + "s    ");
            } else {
                formatStringBuilder.append("%" + widths[c] + "s    ");
            }
        }

        String headerFormat = headerStringBuilder.toString();
        System.out.println(String.format(headerFormat, (Object[]) headers));
        System.out.println(String.format(headerFormat, (Object[]) generateUnderlines(widths)));

        String format = formatStringBuilder.toString();
        for (String[] row : data) {
            System.out.println(String.format(format, (Object[]) row));
        }
    }

    private static String[] generateUnderlines(int[] widths) {
        String[] results = new String[widths.length];
        for (int c = 0; c != widths.length; c++)
            results[c] = "-------------------------------------------------------------------------".substring(0, widths[c]);
        return results;
    }
}
