package com.nomiceu.nomilabs.gregtech.material.registry.register;

import gregtech.api.fluids.FluidBuilder;
import gregtech.api.unification.material.Material;

import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtech.api.unification.material.info.MaterialIconSet.*;
import static com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials.*;

public class LabsTaraniumLine {
    public static void initTaraniumLine() {
        HexafluorosilicicAcid = new Material.Builder(32094, makeLabsName("hexafluorosilicic_acid"))
                .liquid()
                .color(0xd00010)
                .components(Hydrogen, 2, Silicon, 1, Fluorine, 6)
                .build();

        DirtyHexafluorosilicicAcid = new Material.Builder(32095, makeLabsName("dirty_hexafluorosilicic_acid"))
                .liquid()
                .color(0xe00030)
                .flags(DISABLE_DECOMPOSITION)
                .components(Hydrogen, 2, Silicon, 1, Fluorine, 6, RareEarth, 1)
                .build();

        StoneResidue = new Material.Builder(32096, makeLabsName("stone_residue")) // Hardmode Material
                .dust()
                .color(0x4d4d4d).iconSet(ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        UncommonResidue = new Material.Builder(32097, makeLabsName("uncommon_residue")) // Hardmode Material
                .dust()
                .color(0x4d4ded).iconSet(FINE)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        OxidisedResidue = new Material.Builder(32098, makeLabsName("oxidised_residue")) // Hardmode Material
                .dust()
                .color(0xad4d4d).iconSet(FINE)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        RefinedResidue = new Material.Builder(32099, makeLabsName("refined_residue")) // Hardmode Material
                .dust()
                .color(0x2a8a21).iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        CleanInertResidue = new Material.Builder(32100, makeLabsName("clean_inert_residue")) // Hardmode Material
                .dust()
                .color(0x3bbd2f).iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        UltraacidicResidue = new Material.Builder(32101, makeLabsName("ultraacidic_residue"))
                .liquid()
                .color(0xb0babf)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        XenicAcid = new Material.Builder(32102, makeLabsName("xenic_acid"))
                .liquid()
                .color(0xa567db)
                .components(Xenon, 1, Water, 1, Oxygen, 5, HydrogenPeroxide, 1)
                .build();

        XenicAcid.setFormula("H2XeO4", true);

        DustyHelium = new Material.Builder(32103, makeLabsName("dusty_helium"))
                .gas()
                .color(0xa040af)
                .flags(DISABLE_DECOMPOSITION)
                .components(Helium3, 1, RareEarth, 1)
                .build();

        TaraniumEnrichedHelium = new Material.Builder(32104, makeLabsName("taranium_enriched_helium"))
                .gas()
                .plasma()
                .color(0x10c050)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        TaraniumDepletedHelium = new Material.Builder(32105, makeLabsName("taranium_depleted_helium"))
                .gas()
                .color(0x006010)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        TritiumHydride = new Material.Builder(32106, makeLabsName("tritium_hydride"))
                .gas()
                .color(0xd01010)
                .flags(DISABLE_DECOMPOSITION)
                .components(Tritium, 1, Hydrogen, 1)
                .build();

        HeliumHydride = new Material.Builder(32107, makeLabsName("helium_hydride"))
                .gas()
                .color(0xe6d62e)
                .flags(DISABLE_DECOMPOSITION)
                .components(Helium3, 1, Hydrogen, 1)
                .build();

        DioxygenDifluoride = new Material.Builder(32108, makeLabsName("dioxygen_difluoride"))
                .liquid(new FluidBuilder().temperature(80))
                .colorAverage()
                .components(Oxygen, 2, Fluorine, 2)
                .build();
    }
}
