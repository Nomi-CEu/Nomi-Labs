package com.nomiceu.nomilabs.gregtech.material.registry.register;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.info.MaterialIconSet;

import static com.nomiceu.nomilabs.util.RegistryNames.makeLabsName;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.DISABLE_DECOMPOSITION;
import static gregtech.api.unification.material.info.MaterialFlags.GENERATE_PLATE;
import static com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials.*;

public class LabsEndgame {
    public static void initEndgame() {
        CrystalMatrix = new Material.Builder(32023, makeLabsName("crystal_matrix"))
                .ingot().fluid()
                .color(0x70ecff).iconSet(MaterialIconSet.SHINY)
                .flags(GENERATE_PLATE)
                .build();

        DraconicSuperconductor = new Material.Builder(32028, makeLabsName("draconic_superconductor"))
                .ingot()
                .color(0xf5f0f4).iconSet(MaterialIconSet.SHINY)
                .cableProperties(2147483647, 4, 0, true)
                .build();

        KaptonK = new Material.Builder(32050, makeLabsName("kapton_k")) // Hardmode Material
                .ingot().fluid()
                .color(0xffce52).iconSet(MaterialIconSet.DULL)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .components(Carbon, 22, Hydrogen, 10, Nitrogen, 2, Oxygen, 5)
                .build();

        KaptonK.setFormula("C6H2((CO)2N)2C6H4OC6H4", true);
    }
}
