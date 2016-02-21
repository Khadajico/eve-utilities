package com.arrggh.eve.blueprint.utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class DateTimeUtilities {
    private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            .appendLiteral('T')
            .append(DateTimeFormatter.ofPattern("HH:mm:ss")).toFormatter();


    public static LocalDateTime parseDateTime(String value) {
        return LocalDateTime.parse(value, formatter);
    }
}
