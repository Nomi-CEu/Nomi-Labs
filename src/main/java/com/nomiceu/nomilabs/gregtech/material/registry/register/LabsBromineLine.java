package com.nomiceu.nomilabs.gregtech.material.registry.register;

import static com.nomiceu.nomilabs.gregtech.material.registry.LabsMaterials.*;
import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.*;
import static gregtech.api.unification.material.info.MaterialIconSet.*;

import gregtech.api.fluids.FluidBuilder;
import gregtech.api.fluids.attribute.FluidAttributes;
import gregtech.api.unification.material.Material;

public class LabsBromineLine {

    public static void initBromineLine() {
        MineralRichBrine = new Material.Builder(200, makeLabsName("mineralrichbrine"))
                .liquid()
                .color(0xd3dea0)
                .build();

        DepletedBrine = new Material.Builder(201, makeLabsName("depletedbrine"))
                .liquid()
                .color(0xb4b57d)
                .build();

        AlkalineBromineSolution = new Material.Builder(202, makeLabsName("alkalinebrominesolution")) 
                .liquid()
                .color(0xd11a0d)
                .build();

        DebrominatedWaste = new Material.Builder(203, makeLabsName("debrominatedwaste")) 
                .liquid()
                .color(0xd13b0d)
                .build();

        CrudeBromine = new Material.Builder(204, makeLabsName("crudebromine")) // Hardmode Material
                .liquid()
                .color(0xd64d22)
                .build();
      
        BromineChlorineMixture = new Material.Builder(205, makeLabsName("brominechlorinemixture")) // Hardmode Material
                .liquid()
                .color(0xff4f00)
                .build();

    }
}
