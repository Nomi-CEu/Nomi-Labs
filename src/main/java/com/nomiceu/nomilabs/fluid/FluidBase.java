package com.nomiceu.nomilabs.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import com.nomiceu.nomilabs.util.LabsNames;

public class FluidBase extends Fluid {

    private static final ResourceLocation stillLocation = LabsNames.makeLabsName("fluids/fluid_still");
    private static final ResourceLocation flowingLocation = LabsNames.makeLabsName("fluids/fluid_flow");

    public FluidBase(String name, int color, int viscosity, int luminosity) {
        super(name, stillLocation, flowingLocation, color);

        this.viscosity = viscosity;
        this.luminosity = luminosity;
    }
}
