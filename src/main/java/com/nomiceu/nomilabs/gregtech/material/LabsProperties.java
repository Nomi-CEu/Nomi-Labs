package com.nomiceu.nomilabs.gregtech.material;

import gregtech.api.unification.material.info.MaterialIconSet;
import gregtech.api.unification.material.properties.FluidProperty;
import gregtech.api.unification.material.properties.IngotProperty;
import gregtech.api.unification.material.properties.PropertyKey;

import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregicality.multiblocks.api.unification.GCYMMaterials.*;

public class LabsProperties {
    public static void propertyChanges() {
        // Rhodium Sulfate gets Dust Property from adding it as an ore byproduct of Darmstadtite in LabsMicroverse

        Lutetium.setProperty(PropertyKey.INGOT, new IngotProperty());
        Neptunium.setProperty(PropertyKey.INGOT, new IngotProperty());
        Curium.setProperty(PropertyKey.INGOT, new IngotProperty());
        Berkelium.setProperty(PropertyKey.INGOT, new IngotProperty());
        Californium.setProperty(PropertyKey.INGOT, new IngotProperty());
        Einsteinium.setProperty(PropertyKey.INGOT, new IngotProperty());
        Graphite.setProperty(PropertyKey.INGOT, new IngotProperty());

        Neptunium.setProperty(PropertyKey.FLUID, new FluidProperty());
        Curium.setProperty(PropertyKey.FLUID, new FluidProperty());
        Berkelium.setProperty(PropertyKey.FLUID, new FluidProperty());
        Californium.setProperty(PropertyKey.FLUID, new FluidProperty());
        Einsteinium.setProperty(PropertyKey.FLUID, new FluidProperty());
        NetherStar.setProperty(PropertyKey.FLUID, new FluidProperty());
    }

    public static void flagChanges() {
        /* GT 2.8 made Double Plates a separate flag, so some materials lost it. Add it back to needed materials. */

        // GT Materials
        Trinium.addFlags(GENERATE_DOUBLE_PLATE);

        // GCYM Materials
        WatertightSteel.addFlags(GENERATE_DOUBLE_PLATE);
        IncoloyMA956.addFlags(GENERATE_DOUBLE_PLATE);

        /* Other Flag Additions */
        Topaz.addFlags(GENERATE_LENS);
        BlueTopaz.addFlags(GENERATE_LENS);
        EnderPearl.addFlags(GENERATE_LENS);
        Electrum.addFlags(GENERATE_GEAR);
        Neutronium.addFlags(GENERATE_ROUND);
        Titanium.addFlags(GENERATE_FOIL, GENERATE_FINE_WIRE);
        StainlessSteel.addFlags(GENERATE_FOIL, GENERATE_FINE_WIRE);
        NaquadahEnriched.addFlags(GENERATE_BOLT_SCREW);
        Naquadria.addFlags(GENERATE_BOLT_SCREW);
        Trinium.addFlags(GENERATE_DENSE);
        Iridium.addFlags(GENERATE_DENSE);
        Berkelium.addFlags(GENERATE_FRAME);
        BlueSteel.addFlags(GENERATE_FRAME);
        Ultimet.addFlags(GENERATE_FRAME);

        // Prevent TE Gears from showing up
        Tin.addFlags(GENERATE_GEAR);
        Copper.addFlags(GENERATE_GEAR);
        Gold.addFlags(GENERATE_GEAR);
        Lead.addFlags(GENERATE_GEAR);
        Nickel.addFlags(GENERATE_GEAR);
        Platinum.addFlags(GENERATE_GEAR);
        Silver.addFlags(GENERATE_GEAR);
        Emerald.addFlags(GENERATE_GEAR);
    }

    public static void miscChanges() {
        RhodiumSulfate.setMaterialIconSet(MaterialIconSet.ROUGH);

        // No idea why the lang for this is not working
        BlackSteel.setFormula("(AuAgCu3)2Fe3?4", true);
        RhodiumPlatedPalladium.setFormula("((SnFe)4(CuAg4)2)2Pd3Rh", true);
    }
}
