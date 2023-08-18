package com.nomiceu.nomilabs.material;

import gregtech.api.unification.Element;
import gregtech.api.unification.Elements;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.info.MaterialIconSet;
import gregtech.api.unification.material.properties.BlastProperty;

import static com.nomiceu.nomilabs.material.LabsMaterials.*;
import static com.nomiceu.nomilabs.util.RegistryNames.makeLabsName;
import static gregtech.api.unification.material.info.MaterialFlags.*;

public class LabsElements {
    public static final Element Nm = Elements.add(130, 234, -1, null,"Omnium", "Nm", false);
    public static final Element Dc = Elements.add(149, 264, -1, null, "Draconium", "Dc", false);
    public static final Element ADc = Elements.add(149, 264, -1, null, "AwakenedDraconium", "Dc*", false);
    public static final Element Tn = Elements.add(43, 55, -1, null, "Taranium", "Tn", false);

    public static void init() {
        Draconium = new Material.Builder(32001, makeLabsName("draconium"))
                .ingot().fluid().ore()
                .element(LabsElements.Dc)
                .color(0xbe49ed).iconSet(MaterialIconSet.METALLIC)
                .blastTemp(6800, BlastProperty.GasTier.HIGHER)
                .cableProperties(524288, 1, 0, true)
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR, GENERATE_DENSE)
                .build();

        AwakenedDraconium = new Material.Builder(32002, makeLabsName("awakened_draconium"))
                .ingot().fluid()
                .element(LabsElements.ADc)
                .color(0xf58742).iconSet(MaterialIconSet.METALLIC)
                .flags(NO_SMELTING, GENERATE_PLATE, GENERATE_ROD, GENERATE_GEAR)
                .build();

        Omnium = new Material.Builder(32004, makeLabsName("omnium"))
                .ingot().fluid()
                .element(LabsElements.Nm)
                .color(0x84053e).iconSet(MaterialIconSet.SHINY)
                .cableProperties(2147483647, 64, 0, true)
                .build();

        Taranium = new Material.Builder(32109, makeLabsName("taranium")) // Hardmode Material
                .element(LabsElements.Tn)
                .ingot().fluid()
                .color(0xff00ff).iconSet(MaterialIconSet.BRIGHT)
                .flags(GENERATE_PLATE, GENERATE_DENSE)
                .blastTemp(10800)
                .build();

        Taranium.setFormula(\"Tn\")
    }
}
