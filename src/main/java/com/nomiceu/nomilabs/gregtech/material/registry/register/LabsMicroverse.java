package com.nomiceu.nomilabs.gregtech.material.registry.register;

import gregtech.api.unification.material.Material;

import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtech.api.unification.material.info.MaterialIconSet.*;
import static com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials.*;

public class LabsMicroverse {
    public static void initMicroverse() {
        Microversium = new Material.Builder(32027, makeLabsName("microversium"))
                .ingot()
                .color(0x9b61b8).iconSet(DULL)
                .flags(GENERATE_PLATE, GENERATE_FRAME)
                .build();

        Osmiridium8020 = new Material.Builder(32029, makeLabsName("osmiridium_8020"))
                .dust().ore()
                .components(Osmium, 4, Iridium, 1)
                .colorAverage()
                .addOreByproducts(Osmium, Iridium, Ruthenium)
                .build();

        Iridosmine8020 = new Material.Builder(32030, makeLabsName("iridosmine_8020"))
                .dust().ore()
                .components(Iridium, 4, Osmium, 1)
                .colorAverage()
                .addOreByproducts(Iridium, Osmium, Rhodium)
                .build();

        Kaemanite = new Material.Builder(32031, makeLabsName("kaemanite"))
                .dust().ore()
                .components(Trinium, 1, Tantalum, 1, Oxygen, 4)
                .color(0xe7413c).iconSet(BRIGHT)
                .addOreByproducts(Niobium, TriniumSulfide, Trinium)
                .build();

        Fluorite = new Material.Builder(32036, makeLabsName("fluorite"))
                .dust().ore()
                .color(0xFFFC9E).iconSet(ROUGH)
                .components(Calcium, 1, Fluorine, 2)
                .addOreByproducts(Sphalerite, Bastnasite, Topaz)
                .build();

        Snowchestite = new Material.Builder(32060, makeLabsName("snowchestite")) // Hardmode Material
                .dust().ore()
                .flags(DISABLE_DECOMPOSITION)
                .color(0x274c9f).iconSet(SHINY)
                .components(NaquadahOxide, 3, Pyromorphite, 1)
                .addOreByproducts(Chalcopyrite, VanadiumMagnetite, NaquadahHydroxide)
                .build();

        Darmstadtite = new Material.Builder(32110, makeLabsName("darmstadtite"))
                .dust().ore(2, 1)
                .colorAverage().iconSet(DULL)
                .components(Darmstadtium, 2, Sulfur, 3)
                .addOreByproducts(RareEarth, RhodiumSulfate, Darmstadtium)
                .build(); // This also adds dust property to rhodium sulfate

        Dulysite = new Material.Builder(32111, makeLabsName("dulysite"))
                .gem().ore(2, 1)
                .colorAverage().iconSet(RUBY)
                .components(Duranium, 1, Chlorine, 3)
                .addOreByproducts(Sphalerite, Duranium, Europium)
                .build();
    }
}
