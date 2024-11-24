package com.nomiceu.nomilabs.fluid.registry;

import static gregtech.api.fluids.FluidConstants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;

import com.nomiceu.nomilabs.block.registry.LabsBlocks;
import com.nomiceu.nomilabs.fluid.FluidBase;
import com.nomiceu.nomilabs.util.LabsNames;

@SuppressWarnings("unused")
public class LabsFluids {

    private static final List<Fluid> FLUIDS = new ArrayList<>();

    public static final Map<Fluid, Block> BLOCKS = new HashMap<>();

    /**
     * Radioactive Fluids
     */
    public static Fluid URANIUM_233;
    public static Fluid PLUTONIUM_2;

    /**
     * Molten Empowered Fluids
     */
    public static Fluid MOLTEN_EMPOWERED_RESTONIA;
    public static Fluid MOLTEN_EMPOWERED_PALIS;
    public static Fluid MOLTEN_EMPOWERED_ENORI;
    public static Fluid MOLTEN_EMPOWERED_DIAMATINE;
    public static Fluid MOLTEN_EMPOWERED_EMERADIC;
    public static Fluid MOLTEN_EMPOWERED_VOID;

    /**
     * Miscellaneous Fluids
     */
    public static Fluid ELEMENTAL_REDUCTION;
    public static Fluid TOUGH_ALLOY;
    public static Fluid MOLTEN_DARK_SOULARIUM;
    public static Fluid BROMINE_GAS;

    public static void preInit() {
        /* Radioactive Fluids */
        URANIUM_233 = createFluid(new FluidBase("uranium233", 0xff187a30, 1024, 0, 1405));
        PLUTONIUM_2 = createFluid(new FluidBase("plutonium2", 0xfff73663, 1024, 0, 913));

        /* Molten Empowered Fluids */
        MOLTEN_EMPOWERED_RESTONIA = createFluid(
                new FluidBase("moltenempoweredrestonia", 0xffff0000, 10000, 15, SOLID_LIQUID_TEMPERATURE));
        MOLTEN_EMPOWERED_PALIS = createFluid(
                new FluidBase("moltenempoweredpalis", 0xff0026ff, 10000, 15, SOLID_LIQUID_TEMPERATURE));
        MOLTEN_EMPOWERED_ENORI = createFluid(
                new FluidBase("moltenempoweredenori", 0xffe6e6e6, 10000, 15, SOLID_LIQUID_TEMPERATURE));
        MOLTEN_EMPOWERED_DIAMATINE = createFluid(
                new FluidBase("moltenempowereddiamatine", 0xff00fbff, 10000, 15, SOLID_LIQUID_TEMPERATURE));
        MOLTEN_EMPOWERED_EMERADIC = createFluid(
                new FluidBase("moltenempoweredemeradic", 0xff00ff00, 10000, 15, SOLID_LIQUID_TEMPERATURE));
        MOLTEN_EMPOWERED_VOID = createFluid(
                new FluidBase("moltenempoweredvoid", 0xff0e0e0e, 10000, 15, SOLID_LIQUID_TEMPERATURE));

        /* Miscellaneous Fluids */
        ELEMENTAL_REDUCTION = createFluid(new FluidBase("elementalreduction", 0xff588c5a, 2000, 7, ROOM_TEMPERATURE));
        TOUGH_ALLOY = createFluid(new FluidBase("tough_alloy", 0xff10041c, 1024, 0, 1250));
        MOLTEN_DARK_SOULARIUM = createFluid(new FluidBase("moltendarksoularium", 0xff422805, 1000, 0, 8600));
        register();
        BROMINE_GAS = createFluid(new FluidBase("bromine_gas", 0xff500a0a, 2000, 5, ROOM_TEMPERATURE));
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

        BLOCKS.put(fluid, LabsBlocks.createBlockWithoutItem(new BlockFluidClassic(fluid, Material.WATER)
                .setRegistryName(LabsNames.makeLabsName(fluid.getName()))));
    }

    @SideOnly(Side.CLIENT)
    public static void registerFluidBlockModels() {
        FLUIDS.forEach(LabsFluids::registerCustomFluidBlockRenderer);
    }

    @SideOnly(Side.CLIENT)
    public static void registerFluidModels(TextureStitchEvent.Pre event) {
        TextureMap map = event.getMap();
        for (Fluid fluid : FLUIDS) {
            map.registerSprite(fluid.getStill());
            map.registerSprite(fluid.getFlowing());
        }
    }

    /**
     * All the below is Excerpted from Actually Additions.
     * They excerpted it from Tinkers' Construct with permission.
     */
    @SideOnly(Side.CLIENT)
    private static void registerCustomFluidBlockRenderer(Fluid fluid) {
        Block block = fluid.getBlock();
        Item item = Item.getItemFromBlock(block);
        FluidStateMapper mapper = new FluidStateMapper(fluid);
        ModelBakery.registerItemVariants(item);
        ModelLoader.setCustomMeshDefinition(item, mapper);
        ModelLoader.setCustomStateMapper(block, mapper);
    }

    @SideOnly(Side.CLIENT)
    public static class FluidStateMapper extends StateMapperBase implements ItemMeshDefinition {

        public final ModelResourceLocation location;

        public FluidStateMapper(Fluid fluid) {
            this.location = new ModelResourceLocation(LabsNames.makeLabsName("fluids"), fluid.getName());
        }

        @Override
        protected @NotNull ModelResourceLocation getModelResourceLocation(@NotNull IBlockState state) {
            return this.location;
        }

        @Override
        public @NotNull ModelResourceLocation getModelLocation(@NotNull ItemStack stack) {
            return this.location;
        }
    }
}
