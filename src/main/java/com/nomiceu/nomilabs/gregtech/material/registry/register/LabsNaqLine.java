package com.nomiceu.nomilabs.gregtech.material.registry.register;

import gregtech.api.unification.material.Material;

import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtech.api.unification.material.info.MaterialIconSet.*;
import static com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials.*;

public class LabsNaqLine {
    public static void initNaqLine() {
        NaquadahOxide = new Material.Builder(32057, makeLabsName("naquadah_oxide")) // Hardmode Material
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0x17ddd3).iconSet(ROUGH)
                .components(Naquadah, 2, Oxygen, 3)
                .build();

        Pyromorphite = new Material.Builder(32058, makeLabsName("pyromorphite")) // Hardmode Material
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0xd3ed28).iconSet(ROUGH)
                .components(Lead, 5, Phosphate, 3, Chlorine, 1)
                .build();

        NaquadahHydroxide = new Material.Builder(32059, makeLabsName("naquadah_hydroxide")) // Hardmode Material
                .dust()
                .color(0x1941a6).iconSet(DULL)
                .components(Naquadah, 1, Hydrogen, 3, Oxygen, 3)
                .build();

        NaquadahHydroxide.setFormula("Nq(OH)3", true);

        CaesiumHydroxide = new Material.Builder(32061, makeLabsName("caesium_hydroxide")) // Hardmode Material
                .dust()
                .flags(DISABLE_DECOMPOSITION)
                .color(0xbd8340).iconSet(DULL)
                .components(Caesium, 1, Oxygen, 1, Hydrogen, 1)
                .build();

        Neocryolite = new Material.Builder(32062, makeLabsName("neocryolite")) // Hardmode Material
                .fluid()
                .flags(DISABLE_DECOMPOSITION)
                .color(0x3fd1aa)
                .components(Caesium, 3, Naquadah, 1, Fluorine, 6)
                .build();

        NaquadahOxidePetroSolution = new Material.Builder(32063, makeLabsName("naquadah_oxide_petro_solution")) // Hardmode Material
                .fluid()
                .flags(DISABLE_DECOMPOSITION)
                .color(0x595c70)
                .build();

        NaquadahOxideAeroSolution = new Material.Builder(32064, makeLabsName("naquadah_oxide_aero_solution")) // Hardmode Material
                .fluid()
                .flags(DISABLE_DECOMPOSITION)
                .color(0x6f7059)
                .build();

        HotNaquadahOxideNeocryoliteSolution = new Material.Builder(32065, makeLabsName("hot_naquadah_oxide_neocryolite_solution")) // Hardmode Material
                .fluid()
                .flags(DISABLE_DECOMPOSITION)
                .color(0x658280)
                .fluidTemp(4700)
                .build();
    }
}
