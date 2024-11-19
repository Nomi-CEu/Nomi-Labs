package com.nomiceu.nomilabs.gregtech.material.registry.register;

import static com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials.*;
import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static gregtech.api.GTValues.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtech.api.unification.material.info.MaterialIconSet.*;

import gregtech.api.unification.material.Material;

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
                .cableProperties(V[MAX], 16, 0, true)
                .build();

        KaptonK = new Material.Builder(50, makeLabsName("kapton_k")) // Hardmode Material
                .ingot().liquid()
                .color(0xffce52).iconSet(DULL)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .components(Oxydianiline, 3, PyromelliticDianhydride, 2)
                .build();
    }
}
