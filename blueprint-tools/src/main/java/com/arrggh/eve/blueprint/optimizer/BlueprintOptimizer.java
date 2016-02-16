package com.arrggh.eve.blueprint.optimizer;

import com.arrggh.eve.blueprint.data.BlueprintLoader;
import com.arrggh.eve.blueprint.data.PriceQuery;
import com.arrggh.eve.blueprint.data.TypeLoader;
import com.arrggh.eve.blueprint.model.EveBlueprint;
import com.arrggh.eve.blueprint.model.EveMaterial;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static org.apache.logging.log4j.LogManager.getLogger;

public class BlueprintOptimizer {
    private static final Logger LOG = getLogger(BlueprintOptimizer.class);
    private final EveBlueprint blueprint;
    private final TypeLoader typeLoader;
    private final BlueprintLoader blueprintLoader;
    private final PriceQuery priceQuery;

    public BlueprintOptimizer(TypeLoader typeLoader, BlueprintLoader blueprintLoader, PriceQuery priceQuery, String blueprintName) {
        this.typeLoader = typeLoader;
        this.blueprintLoader = blueprintLoader;
        this.priceQuery = priceQuery;

        Optional<EveBlueprint> blueprintOptional = blueprintLoader.getBlueprint(blueprintName);
        if (!blueprintOptional.isPresent()) {
            throw new IllegalArgumentException("Cannot find a blueprint called '" + blueprintName + "'");
        }
        blueprint = blueprintOptional.get();
    }

    public void optimize() {
        System.out.println("Starting optimization of blueprint '" + blueprint.getName() + "'");

        optimize("", blueprint);
    }

    private void optimize(String space, EveBlueprint bp) {
        System.out.println(String.format("%s %s", space, bp.getName()));

        EveMaterial produces = bp.getManufacture().getProduces().get(0);
        System.out.println(String.format("%s %s -> @ %f", space, produces.getName(), priceQuery.queryPrice(produces.getTypeId()) * produces.getQuantity()));

        for (EveMaterial material : bp.getManufacture().getMaterials()) {
            Optional<EveBlueprint> blueprintOptional = blueprintLoader.getBlueprintForId(material.getTypeId());
            if (blueprintOptional.isPresent()) {
                optimize(space + "  ", blueprintOptional.get());
            } else {
                System.out.println(String.format("%s  %-20s x %10d", space , material.getName(), material.getQuantity()));
            }
        }
    }
}
