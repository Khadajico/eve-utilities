/*
 * Copyright (C) 2016 Khadajico
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */
package com.arrggh.eve.blueprint.model;

import com.arrggh.eve.blueprint.data.TypeLoader;
import lombok.Data;

import java.util.List;

@Data
public class EveManufacture {
    private List<EveMaterial> materials;
    private List<EveMaterial> produces;
    private List<EveSkill> skills;
}
