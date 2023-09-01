package com.nomiceu.nomilabs.fluid.registry;

import com.nomiceu.nomilabs.block.registry.LabsBlocks;
import com.nomiceu.nomilabs.fluid.FluidBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.ArrayList;
import java.util.List;

import static com.nomiceu.nomilabs.LabsValues.CONTENTTWEAKER_MODID;

public class LabsFluids {

    private static final List<Fluid> FLUIDS = new ArrayList<>();

    public static Fluid ELEMENTAL_REDUCTION;

    public static void preInit() {
        ELEMENTAL_REDUCTION = createFluid(new FluidBase("elementalreduction", 0x588c5a, 2000, 0));
        register();
    }

    /* HELPER FUNCTIONS */
    public static <T extends Fluid> T createFluid(T fluid) {
        FLUIDS.add(fluid);
        return fluid;
    }

    public static void register() {
        for (Fluid fluid : FLUIDS) {
            registerFluid(fluid);
        }
    }

    private static void registerFluid(Fluid fluid) {
        FluidRegistry.registerFluid(fluid);
        FluidRegistry.addBucketForFluid(fluid);

        createBlockForFluid(fluid);
    }

    private static void createBlockForFluid(Fluid fluid) {
        BlockFluidClassic blockFluid = new BlockFluidClassic(fluid, Material.WATER);

        blockFluid.setRegistryName(CONTENTTWEAKER_MODID, fluid.getName());

        LabsBlocks.createBlockWithoutItem(blockFluid);
    }

    public static void registerModels(TextureStitchEvent.Pre event) {
        TextureMap map = event.getMap();
        for (Fluid fluid : FLUIDS) {
            map.registerSprite(fluid.getStill());
            map.registerSprite(fluid.getFlowing());
        }
    }
}
