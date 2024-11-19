package com.nomiceu.nomilabs.gregtech.material.registry.register;

import static com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials.*;
import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtech.api.unification.material.info.MaterialIconSet.*;

import gregtech.api.fluids.FluidBuilder;
import gregtech.api.unification.material.Material;

public class LabsRareEarthLine {

    public static void initRareEarthLine() {
        RareEarthOxideConcentrate = new Material.Builder(118, makeLabsName("rare_earth_oxide_concentrate")) // Hardmode
                                                                                                            // Material
                .dust()
                .color(0x394c04).iconSet(FINE)
                .flags(DISABLE_DECOMPOSITION)
                .build();
        RoastedRareEarthOxideConcentrate = new Material.Builder(119,
                makeLabsName("roasted_rare_earth_oxide_concentrate")) // Hardmode Material
                        .dust()
                        .color(0x182100).iconSet(ROUGH)
                        .flags(DISABLE_DECOMPOSITION)
                        .build();
        LeechedRareEarthOxide = new Material.Builder(120, makeLabsName("leeched_rare_earth_oxide")) // Hardmode
                                                                                                    // Material
                .dust()
                .color(0x4c5632).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .build();
        TrivalentRareEarths = new Material.Builder(121, makeLabsName("trivalent_rare_earths")) // Hardmode Material
                .dust()
                .color(0xa7ea8c).iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .build();
        CeriumConcentrate = new Material.Builder(122, makeLabsName("cerium_concentrate")) // Hardmode Material
                .dust()
                .color(0xef654c).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .build();
        DissolvedCeriumConcentrate = new Material.Builder(123, makeLabsName("dissolved_cerium_concentrate")) // Hardmode
                                                                                                             // Material
                .liquid()
                .color(0xc1796c)
                .flags(DISABLE_DECOMPOSITION)
                .build();
        VaporousNitricAcid = new Material.Builder(124, makeLabsName("vaporous_nitric_acid"))
                .liquid(new FluidBuilder().temperature(356))
                .color(0xeaf293)
                .components(Hydrogen, 1, Nitrogen, 1, Oxygen, 3)
                .build();
    }
}
