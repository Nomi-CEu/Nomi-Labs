package com.nomiceu.nomilabs.gregtech.material.registry.register;

import static com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials.*;
import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static gregtech.api.GTValues.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtech.api.unification.material.info.MaterialIconSet.*;

import gregtech.api.unification.Element;
import gregtech.api.unification.Elements;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.properties.BlastProperty.GasTier;

public class LabsElements {

    public static final Element Nm = Elements.add(130, 234, -1, null, "Omnium", "Nm", false);
    public static final Element Dc = Elements.add(149, 264, -1, null, "Draconium", "Dc", false);
    public static final Element ADc = Elements.add(149, 264, -1, null, "AwakenedDraconium", "Dc*", false);
    public static final Element Tn = Elements.add(43, 55, -1, null, "Taranium", "Tn", false);
    public static final Element Sc = Elements.add( 21, 45, -1, null, "Scandium", "Sc", false );
    public static final Element Yb = Elements.add(70, 174, -1, null, "Ytterbium", "Yb", false);
    public static final Element Pr = Elements.add(59, 141, -1, null,"Praseodymium","Pr",false);
    public static final Element Gd = Elements.add(64, 158, -1, null, "Gadolinium", "Gd", false);

    public static void init() {
        Draconium = new Material.Builder(1, makeLabsName("draconium"))
                .ingot().liquid().ore()
                .element(Dc)
                .color(0xbe49ed).iconSet(METALLIC)
                .blast(builder -> builder
                        .temp(6800, GasTier.HIGHER)
                        .blastStats(VA[LuV], 1800)
                        .vacuumStats(VA[EV], 600))
                .cableProperties(V[UV], 1, 0, true)
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

        Scandium = new Material.Builder(118, makeLabsName("Scandium"))
                .element(Sc)
                .ingot().liquid()
                .color(0xa4c1c4).iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_ROD)
                .blast(builder -> builder
                        .temp(1800, GasTier.LOW)
                        .blastStats(VA[HV], 400))
                .build();

        Ytterbium = new  Material.Builder(119, makeLabsName("Ytterbium"))
                .element(Yb)
                .ingot().liquid()
                .color(0xd4ddad).iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_BOLT_SCREW, GENERATE_FINE_WIRE)
                .blast(builder -> builder
                        .temp(1100, GasTier.LOW)
                        .blastStats(VA[LV], 400))
                .build();

        Praseodymium = new Material.Builder(120, makeLabsName("Praseodymium"))
                .element(Pr)
                .ingot().liquid()
                .color(0xdeea9d).iconSet(METALLIC)
                .flags(IS_MAGNETIC, GENERATE_ROD, GENERATE_FINE_WIRE, GENERATE_RING, GENERATE_FOIL)
                .blast(builder -> builder
                        .temp(1100, GasTier.LOW)
                        .blastStats(VA[LV],400))
                .build();

        Gadolinium = new  Material.Builder(121, makeLabsName("Gadolinium"))
                .element(Gd)
                .ingot().liquid()
                .color(0xf2bbae).iconSet(METALLIC)
                .flags(IS_MAGNETIC, GENERATE_ROD, GENERATE_FINE_WIRE, GENERATE_RING, GENERATE_FOIL, GENERATE_PLATE, GENERATE_BOLT_SCREW, GENERATE_ROUND)
                .blast(builder -> builder
                        .temp(1600, GasTier.MID)
                        .blastStats(VA[HV],800))
                .build();







    }
}
