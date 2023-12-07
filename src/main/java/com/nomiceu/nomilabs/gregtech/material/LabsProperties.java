package com.nomiceu.nomilabs.gregtech.material;

import gregtech.api.unification.material.info.MaterialIconSet;
import gregtech.api.unification.material.properties.FluidProperty;
import gregtech.api.unification.material.properties.IngotProperty;
import gregtech.api.unification.material.properties.PropertyKey;

import static com.nomiceu.nomilabs.gregtech.prefix.LabsMaterialFlags.GENERATE_PERFECT_GEM;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtech.api.unification.material.info.MaterialFlags.GENERATE_GEAR;

public class LabsProperties {
    public static void propertyChanges() {
        //RhodiumSulfate.setProperty(PropertyKey.DUST, new DustProperty(0, 0)); // GT added maybe?

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

        // Add Perfect Gems
        Diamond.addFlags(GENERATE_PERFECT_GEM);
        Emerald.addFlags(GENERATE_PERFECT_GEM);
        Ruby.addFlags(GENERATE_PERFECT_GEM);
        Topaz.addFlags(GENERATE_PERFECT_GEM);
    }

    public static void miscChanges() {
        RhodiumSulfate.setMaterialIconSet(MaterialIconSet.ROUGH);

        // No idea why the lang for this is not working
        BlackSteel.setFormula("(AuAgCu3)2Fe3?4", true);
        RhodiumPlatedPalladium.setFormula("((SnFe)4(CuAg4)2)2Pd3Rh", true);
    }
}
