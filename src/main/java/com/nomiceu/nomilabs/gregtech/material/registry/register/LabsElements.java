package com.nomiceu.nomilabs.gregtech.material.registry.register;

import static com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials.*;
import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static gregtech.api.GTValues.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtech.api.unification.material.info.MaterialIconSet.*;

import gregtech.api.fluids.FluidBuilder;
import gregtech.api.unification.Element;
import gregtech.api.unification.Elements;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.properties.BlastProperty.GasTier;

public class LabsElements {

    public static final Element Nm = Elements.add(130, 234, -1, null, "Omnium", "Nm", false);
    public static final Element Dc = Elements.add(149, 264, -1, null, "Draconium", "Dc", false);
    public static final Element ADc = Elements.add(149, 264, -1, null, "AwakenedDraconium", "Dc*", false);
    public static final Element Tn = Elements.add(43, 55, -1, null, "Taranium", "Tn", false);
    public static final Element Tc = Elements.add(43, 56, -1, null, "Technetium", "Tc", false);

    public static void init() {
        Draconium = new Material.Builder(1, makeLabsName("draconium"))
                .ingot().liquid().ore()
                .element(Dc)
                .color(0xbe49ed).iconSet(METALLIC)
                .blast(builder -> builder
                        .temp(6800, GasTier.HIGHER)
                        .blastStats(VA[LuV], 1800)
                        .vacuumStats(VA[EV], 600))
                .cableProperties(V[UV], 8, 0, true)
                .flags(GENERATE_PLATE, GENERATE_DOUBLE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_DENSE)
                .build();

        AwakenedDraconium = new Material.Builder(2, makeLabsName("awakened_draconium"))
                .ingot().liquid()
                .element(ADc)
                .color(0xf58742).iconSet(METALLIC)
                .flags(NO_SMELTING, GENERATE_PLATE, GENERATE_DOUBLE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_FRAME,
                        GENERATE_RING)
                .build();

        Omnium = new Material.Builder(4, makeLabsName("omnium"))
                .ingot().liquid()
                .element(Nm)
                .color(0x84053e).iconSet(SHINY)
                .cableProperties(V[MAX], 64, 0, true)
                .build();
        Technetium = new Material.Builder(20, makeLabsName("technetium"))
                .ingot().liquid(new FluidBuilder().temperature(2500))
                .element(Tc)
                .color(0xcadce2).iconSet(SHINY)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_BOLT_SCREW, GENERATE_FOIL)
                .cableProperties(V[UHV], 2, 0, true)
                .build();

        Taranium = new Material.Builder(109, makeLabsName("taranium")) // Hardmode Material
                .element(Tn)
                .ingot().liquid()
                .color(0xff00ff).iconSet(BRIGHT)
                .flags(GENERATE_PLATE, GENERATE_DENSE)
                .blast(builder -> builder
                        .temp(10800, GasTier.HIGHEST)
                        .blastStats(VA[ZPM], 1800)
                        .vacuumStats(VA[IV], 600))
                .build();
    }
}
