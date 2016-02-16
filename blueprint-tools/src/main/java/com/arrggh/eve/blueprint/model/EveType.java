package com.arrggh.eve.blueprint.model;

import lombok.Data;

@Data
public class EveType {
    private int id;
    private String name;
    private String description;
    private int iconId;
    private int marketGroupId;
    private boolean published;
}
