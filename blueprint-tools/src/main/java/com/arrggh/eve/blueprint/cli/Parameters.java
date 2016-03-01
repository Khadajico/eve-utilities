/*
 * Copyright (C) 2016 Khadajico
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package com.arrggh.eve.blueprint.cli;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Parameters {
    private final boolean debug;
    private final boolean verbose;
    private final boolean optimize;
    private final boolean locate;
    private final boolean version;

    private final int limit;

    private final String blueprintName;
    private final String searchString;
    private final String priceCache;
}
