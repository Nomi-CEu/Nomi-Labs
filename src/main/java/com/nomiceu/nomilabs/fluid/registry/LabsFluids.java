package com.nomiceu.nomilabs.fluid.registry;

import com.nomiceu.nomilabs.block.registry.LabsBlocks;
import com.nomiceu.nomilabs.fluid.FluidBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    @SideOnly(Side.CLIENT)
    public static void registerFluidBlockModels() {
        FLUIDS.forEach(LabsFluids::registerCustomFluidBlockRenderer);
        /* invis fluids
        BakedModelHandler modelHandler = new BakedModelHandler();
        MinecraftForge.EVENT_BUS.register(modelHandler);
        FLUID_BLOCKS.forEach(modelHandler::addFluidBlock);
         */
    }

    /**
     * (Excerpted from Tinkers' Construct with permission, thanks guys!)
     */
    private static void registerCustomFluidBlockRenderer(Fluid fluid) {
        Block block = fluid.getBlock();
        Item item = Item.getItemFromBlock(block);
        FluidStateMapper mapper = new FluidStateMapper(fluid);
        ModelBakery.registerItemVariants(item);
        ModelLoader.setCustomMeshDefinition(item, mapper);
        ModelLoader.setCustomStateMapper(block, mapper);
    }


    @SideOnly(Side.CLIENT)
    public static void registerFluidModels(TextureStitchEvent.Pre event) {
        TextureMap map = event.getMap();
        for (Fluid fluid : FLUIDS) {
            map.registerSprite(fluid.getStill());
            map.registerSprite(fluid.getFlowing());
        }
    }
}
