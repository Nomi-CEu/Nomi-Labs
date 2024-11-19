package com.nomiceu.nomilabs.gregtech.material.registry.register;

import gregtech.api.fluids.FluidBuilder;
import gregtech.api.unification.material.Material;

import static com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials.*;
import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.DISABLE_DECOMPOSITION;
import static gregtech.api.unification.material.info.MaterialIconSet.*;

public class LabsRareEarthLine {
    public static void initRareEarthLine() {

        RareEarthOxideConcentrate = new Material.Builder(122, makeLabsName("rare_earth_oxide_concentrate"))
                .dust()
                .color(0x394c04).iconSet(FINE)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        RoastedRareEarthOxideConcentrate = new Material.Builder(123, makeLabsName("roasted_rare_earth_oxide_concentrate"))
                .dust()
                .color(0x182100).iconSet(ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        LeechedRareEarthOxide = new Material.Builder(124, makeLabsName("leeched_rare_earth_oxide"))
                .dust()
                .color(0x4c5632).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        TrivalentRareEarths = new Material.Builder(125, makeLabsName("trivalent_rare_earths"))
                .dust()
                .color(0xa7ea8c).iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        CeriumConcentrate = new Material.Builder(126, makeLabsName("cerium_concentrate"))
                .dust()
                .color(0xef654c).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        DissolvedCeriumConcentrate = new Material.Builder(127, makeLabsName("dissolved_cerium_concentrate"))
                .liquid()
                .color(0xc1796c)
                .flags(DISABLE_DECOMPOSITION)
                .build();

        VaporousNitricAcid = new Material.Builder(128, makeLabsName("vaporous_nitric_acid"))
                .gas(new FluidBuilder().temperature(356))
                .color(0xeaf293)
                .components(Nitrogen, 1, Hydrogen, 1, Oxygen, 3)
                .build();

    }
}
