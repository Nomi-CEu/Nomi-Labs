package com.nomiceu.nomilabs.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import static com.nomiceu.nomilabs.LabsValues.CONTENTTWEAKER_MODID;

public class FluidBase extends Fluid {
    private static final ResourceLocation stillLocation = new ResourceLocation(CONTENTTWEAKER_MODID, "fluids/fluid_still");
    private static final ResourceLocation flowingLocation = new ResourceLocation(CONTENTTWEAKER_MODID, "fluids/fluid_flow");

    public FluidBase(String name, int color, int viscosity, int luminosity) {
        this(name, color, viscosity, luminosity, 1000);
    }
    public FluidBase(String name, int color, int viscosity, int luminosity, int density) {
        super(name, stillLocation, flowingLocation, color);

        this.viscosity = viscosity;
        this.luminosity = luminosity;
        this.density = density;
    }
}
