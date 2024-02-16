package com.nomiceu.nomilabs.gregtech.material.registry.register;

import gregtech.api.unification.material.Material;

import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static gregtech.api.GTValues.*;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtech.api.unification.material.info.MaterialIconSet.*;
import static com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials.*;

public class LabsEndgame {
    public static void initEndgame() {
        CrystalMatrix = new Material.Builder(23, makeLabsName("crystal_matrix"))
                .ingot().liquid()
                .color(0x70ecff).iconSet(SHINY)
                .flags(GENERATE_PLATE, GENERATE_DOUBLE_PLATE)
                .build();

        DraconicSuperconductor = new Material.Builder(28, makeLabsName("draconic_superconductor"))
                .ingot()
                .color(0xf5f0f4).iconSet(SHINY)
                .cableProperties(V[MAX], 4, 0, true)
                .build();

        KaptonK = new Material.Builder(50, makeLabsName("kapton_k")) // Hardmode Material
                .ingot().liquid()
                .color(0xffce52).iconSet(DULL)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .components(Carbon, 22, Hydrogen, 10, Nitrogen, 2, Oxygen, 5)
                .build();

        KaptonK.setFormula("C6H2((CO)2N)2C6H4OC6H4", true);
    }
}
