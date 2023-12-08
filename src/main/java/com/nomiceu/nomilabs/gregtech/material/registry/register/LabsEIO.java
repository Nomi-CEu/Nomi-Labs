package com.nomiceu.nomilabs.gregtech.material.registry.register;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.properties.BlastProperty.GasTier;
import gregtech.api.unification.material.properties.ToolProperty;

import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static gregtech.api.GTValues.*;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtech.api.unification.material.info.MaterialIconSet.*;
import static com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials.*;

public class LabsEIO {
    public static void initEIO() {
        DarkSteel = new Material.Builder(32003, makeLabsName("dark_steel"))
                .ingot()
                .liquid()
                .color(0x414751).iconSet(DULL)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_FRAME, DISABLE_DECOMPOSITION)
                .components(Iron, 1)
                .build();

        ConductiveIron = new Material.Builder(32011, makeLabsName("conductive_iron"))
                .ingot()
                .liquid()
                .color(0xf7b29b).iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_GEAR)
                .components(Iron, 1, Redstone, 1)
                .cableProperties(V[LV], 1, 0, true)
                .build();

        EnergeticAlloy = new Material.Builder(32012, makeLabsName("energetic_alloy"))
                .ingot()
                .liquid()
                .color(0xffb545).iconSet(SHINY)
                .flags(GENERATE_PLATE, GENERATE_GEAR)
                .blast(builder -> builder
                        .temp(1250, GasTier.LOW)
                        .blastStats(VA[MV], 400))
                .components(Gold, 2, Redstone, 1, Glowstone, 1)
                .cableProperties(V[MV], 1, 0, true)
                .build();

        VibrantAlloy = new Material.Builder(32013, makeLabsName("vibrant_alloy"))
                .ingot()
                .liquid()
                .color(0xa4ff70).iconSet(SHINY)
                .flags(GENERATE_PLATE, GENERATE_GEAR, GENERATE_ROD, GENERATE_BOLT_SCREW)
                .blast(builder -> builder
                        .temp(1350, GasTier.LOW)
                        .blastStats(VA[HV], 400))
                .components(EnergeticAlloy, 1, EnderPearl, 1)
                .cableProperties(V[MV], 1, 0, true)
                .build();

        PulsatingIron = new Material.Builder(32014, makeLabsName("pulsating_iron"))
                .ingot()
                .liquid()
                .color(0x6ae26e).iconSet(SHINY)
                .flags(GENERATE_PLATE, GENERATE_GEAR)
                .components(Iron, 1)
                .cableProperties(V[ULV], 1, 0, true)
                .build();

        ElectricalSteel = new Material.Builder(32015, makeLabsName("electrical_steel"))
                .ingot()
                .liquid()
                .color(0xb2c0c1).iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_GEAR)
                .components(Steel, 1, Silicon, 1)
                .build();

        Soularium = new Material.Builder(32024, makeLabsName("soularium"))
                .ingot()
                .liquid()
                .color(0x7c674d).iconSet(METALLIC)
                .flags(GENERATE_PLATE)
                .components(Gold, 1)
                .build();

        EndSteel = new Material.Builder(32025, makeLabsName("end_steel"))
                .ingot()
                .liquid()
                .color(0xd6d980).iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_GEAR)
                .toolStats(ToolProperty.Builder.of(4.0f, 3.5f, 1024, 3).build())
                .cableProperties(V[EV], 1, 0, true)
                .build();
    }
}
