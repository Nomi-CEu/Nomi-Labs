package com.nomiceu.nomilabs.gregtech.material.registry.register;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.properties.BlastProperty.GasTier;

import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static gregtech.api.GTValues.*;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtech.api.unification.material.info.MaterialIconSet.*;
import static com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials.*;

public class LabsThermal {
    public static void initThermal() {
        Ardite = new Material.Builder(32006, makeLabsName("ardite"))
                .ingot()
                .liquid()
                .color(0xad2f05).iconSet(DULL)
                .flags(GENERATE_PLATE)
                .components(RedSteel, 3, Blaze, 1)
                .build();

        Mana = new Material.Builder(32007, makeLabsName("mana"))
                .flags(DISABLE_DECOMPOSITION)
                .build();

        Manyullyn = new Material.Builder(32008, makeLabsName("manyullyn"))
                .ingot()
                .liquid()
                .color(0x9949cc).iconSet(METALLIC)
                .flags(GENERATE_PLATE)
                .components(Ardite, 4, Cobalt, 4, Mana, 1)
                .build();

        Signalum = new Material.Builder(32010, makeLabsName("signalum"))
                .ingot()
                .liquid()
                .color(0xff7f0f).iconSet(SHINY)
                .blast(builder -> builder
                        .temp(4000, GasTier.MID)
                        .blastStats(VA[IV], 1400)
                        .vacuumStats(VA[HV], 500))
                .flags(GENERATE_PLATE, GENERATE_DENSE, GENERATE_ROD, GENERATE_GEAR)
                .components(AnnealedCopper, 4, Ardite, 2, RedAlloy, 2)
                .cableProperties(V[LuV], 1, 0, true)
                .build();

        Lumium = new Material.Builder(32017, makeLabsName("lumium"))
                .ingot()
                .liquid()
                .color(0xf6ff99).iconSet(BRIGHT)
                .flags(GENERATE_PLATE, GENERATE_GEAR, GENERATE_FINE_WIRE)
                .blast(builder -> builder
                        .temp(4500, GasTier.MID)
                        .blastStats(VA[IV], 1600)
                        .vacuumStats(VA[HV], 600))
                .components(TinAlloy, 4, SterlingSilver, 2)
                .cableProperties(V[IV], 1, 0, true)
                .build();

        Enderium = new Material.Builder(32018, makeLabsName("enderium"))
                .ingot()
                .liquid()
                .color(0x1f6b62).iconSet(SHINY)
                .flags(GENERATE_PLATE, GENERATE_GEAR, GENERATE_FINE_WIRE)
                .blast(builder -> builder
                        .temp(6400, GasTier.HIGHEST)
                        .blastStats(VA[LuV], 1200)
                        .vacuumStats(VA[EV], 400))
                .components(Lead, 4, Platinum, 2, BlueSteel, 1, Osmium, 1)
                .cableProperties(V[ZPM], 1, 0, true)
                .build();

        ElectrumFlux = new Material.Builder(32019, makeLabsName("electrum_flux"))
                .ingot()
                .liquid()
                .color(0xf7be20).iconSet(BRIGHT)
                .flags(GENERATE_PLATE, GENERATE_GEAR)
                .blast(1100)
                .components(Electrum, 6, Lumium, 1, Signalum, 1)
                .build();

        Mithril = new Material.Builder(32021, makeLabsName("mithril"))
                .ingot()
                .color(0x428fdb).iconSet(DULL)
                .flags(GENERATE_PLATE, GENERATE_GEAR, NO_UNIFICATION)
                .components(Titanium, 1, Mana, 1)
                .build();
    }
}
