package com.nomiceu.nomilabs.gregtech.material.registry.register;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.properties.BlastProperty;

import static com.nomiceu.nomilabs.util.RegistryNames.makeLabsName;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtech.api.unification.material.info.MaterialIconSet.*;
import static com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials.*;

public class LabsThermal {
    public static void initThermal() {
        Ardite = new Material.Builder(32006, makeLabsName("ardite"))
                .ingot().fluid()
                .color(0xad2f05).iconSet(DULL)
                .flags(GENERATE_PLATE)
                .components(RedSteel, 3, Blaze, 1)
                .build();

        Mana = new Material.Builder(32007, makeLabsName("mana"))
                .flags(DISABLE_DECOMPOSITION)
                .build();

        Manyullyn = new Material.Builder(32008, makeLabsName("manyullyn"))
                .ingot().fluid()
                .color(0x9949cc).iconSet(METALLIC)
                .flags(GENERATE_PLATE)
                .components(Ardite, 4, Cobalt, 4, Mana, 1)
                .build();

        Signalum = new Material.Builder(32010, makeLabsName("signalum"))
                .ingot().fluid()
                .color(0xff7f0f).iconSet(SHINY)
                .blastTemp(4000, BlastProperty.GasTier.MID, 120, 12800)
                .flags(GENERATE_PLATE, GENERATE_DENSE, GENERATE_ROD, GENERATE_GEAR)
                .components(AnnealedCopper, 4, Ardite, 2, RedAlloy, 2)
                .cableProperties(32768, 1, 0, true)
                .build();

        Lumium = new Material.Builder(32017, makeLabsName("lumium"))
                .ingot().fluid()
                .color(0xf6ff99).iconSet(BRIGHT)
                .flags(GENERATE_PLATE, GENERATE_GEAR, GENERATE_FINE_WIRE)
                .blastTemp(4500, BlastProperty.GasTier.MID, 120, 14400)
                .components(TinAlloy, 4, SterlingSilver, 2)
                .cableProperties(8192, 1, 0, true)
                .build();

        Enderium = new Material.Builder(32018, makeLabsName("enderium"))
                .ingot().fluid()
                .color(0x1f6b62).iconSet(SHINY)
                .flags(GENERATE_PLATE, GENERATE_GEAR, GENERATE_FINE_WIRE)
                .blastTemp(6400, BlastProperty.GasTier.HIGHEST, 120, 20800)
                .components(Lead, 4, Platinum, 2, BlueSteel, 1, Osmium, 1)
                .cableProperties(131072, 1, 0, true)
                .build();

        ElectrumFlux = new Material.Builder(32019, makeLabsName("electrum_flux"))
                .ingot().fluid()
                .color(0xf7be20).iconSet(BRIGHT)
                .flags(GENERATE_PLATE, GENERATE_GEAR)
                .blastTemp(1100)
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
