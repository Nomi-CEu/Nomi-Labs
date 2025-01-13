package com.nomiceu.nomilabs.gregtech.material.registry.register;

import static com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials.*;
import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtech.api.unification.material.info.MaterialIconSet.*;

import gregtech.api.fluids.FluidBuilder;
import gregtech.api.fluids.attribute.FluidAttributes;
import gregtech.api.unification.material.Material;

public class LabsTaraniumLine {

    public static void initTaraniumLine() {
        HexafluorosilicicAcid = new Material.Builder(94, makeLabsName("hexafluorosilicic_acid"))
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0xd00010)
                .components(Hydrogen, 2, Silicon, 1, Fluorine, 6)
                .build();

        DirtyHexafluorosilicicAcid = new Material.Builder(95, makeLabsName("dirty_hexafluorosilicic_acid"))
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0xe00030)
                .flags(DISABLE_DECOMPOSITION)
                .components(Hydrogen, 2, Silicon, 1, Fluorine, 6, RareEarth, 1)
                .build();

        StoneResidue = new Material.Builder(96, makeLabsName("stone_residue")) // Hardmode Material
                .dust()
                .color(0x4d4d4d).iconSet(ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        UncommonResidue = new Material.Builder(97, makeLabsName("uncommon_residue")) // Hardmode Material
                .dust()
                .color(0x4d4ded).iconSet(FINE)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        OxidisedResidue = new Material.Builder(98, makeLabsName("oxidised_residue")) // Hardmode Material
                .dust()
                .color(0xad4d4d).iconSet(FINE)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        RefinedResidue = new Material.Builder(99, makeLabsName("refined_residue")) // Hardmode Material
                .dust()
                .color(0x2a8a21).iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        CleanInertResidue = new Material.Builder(100, makeLabsName("clean_inert_residue")) // Hardmode Material
                .dust()
                .color(0x3bbd2f).iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        UltraacidicResidue = new Material.Builder(101, makeLabsName("ultraacidic_residue"))
                .liquid()
                .color(0xb0babf)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        XenicAcid = new Material.Builder(102, makeLabsName("xenic_acid"))
                .liquid()
                .color(0xa567db)
                .components(Xenon, 1, Water, 1, Oxygen, 3)
                .build().setFormula("H2XeO4", true);

        DustyHelium = new Material.Builder(103, makeLabsName("dusty_helium"))
                .gas()
                .color(0xa040af)
                .flags(DISABLE_DECOMPOSITION)
                .components(Helium3, 1, RareEarth, 1)
                .build();

        TaraniumEnrichedHelium = new Material.Builder(104, makeLabsName("taranium_enriched_helium"))
                .gas()
                .plasma()
                .color(0x10c050)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        TaraniumDepletedHelium = new Material.Builder(105, makeLabsName("taranium_depleted_helium"))
                .gas()
                .color(0x006010)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        TritiumHydride = new Material.Builder(106, makeLabsName("tritium_hydride"))
                .gas()
                .color(0xd01010)
                .flags(DISABLE_DECOMPOSITION)
                .components(Tritium, 1, Hydrogen, 1)
                .build();

        HeliumHydride = new Material.Builder(107, makeLabsName("helium_hydride"))
                .gas()
                .color(0xe6d62e)
                .flags(DISABLE_DECOMPOSITION)
                .components(Helium3, 1, Hydrogen, 1)
                .build();

        DioxygenDifluoride = new Material.Builder(108, makeLabsName("dioxygen_difluoride"))
                .liquid(new FluidBuilder().temperature(80))
                .colorAverage()
                .components(Oxygen, 2, Fluorine, 2)
                .build();
    }
}
