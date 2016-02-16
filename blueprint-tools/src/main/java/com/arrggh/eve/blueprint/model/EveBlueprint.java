package com.arrggh.eve.blueprint.model;

import lombok.Data;

@Data
public class EveBlueprint {
private int id;
    private String name;
    private int marketGroupId;
    private EveManufacture manufacture;
}

