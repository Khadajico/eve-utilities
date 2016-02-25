package com.arrggh.eve.blueprint.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EveBlueprint {
    private int id;
    private String name;
    private int marketGroupId;
    private EveManufacture manufacture;
}

