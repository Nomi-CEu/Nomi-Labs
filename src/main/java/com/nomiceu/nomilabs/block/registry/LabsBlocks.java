package com.nomiceu.nomilabs.block.registry;

import static com.nomiceu.nomilabs.util.LabsNames.makeLabsName;
import static com.nomiceu.nomilabs.util.LabsTranslate.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.registries.IForgeRegistry;

import org.jetbrains.annotations.NotNull;

import com.nomiceu.nomilabs.block.*;
import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;
import com.nomiceu.nomilabs.item.ItemBlockBase;
import com.nomiceu.nomilabs.item.ItemExcitationCoil;
import com.nomiceu.nomilabs.item.registry.LabsItems;

import gregtech.api.items.toolitem.ToolClasses;

@SuppressWarnings("unused")
public class LabsBlocks {

    private static final String nullTranslationKey = "tile.null";
    private static final List<Block> BLOCKS = new ArrayList<>();

    public static final Map<Block, Item> ITEMS = new HashMap<>();

    /* Dense Blocks */
    public static BlockBase DENSE_MAGMA;
    public static BlockBase DENSE_OIL_SHALE;

    /* Ultimate Blocks */
    public static BlockBase ULTIMATE_POWER_STORAGE;
    public static BlockBase ULTIMATE_GENERATOR;

    /* Misc Blocks */
    public static BlockBase MICROVERSE_CASING;

    /* Custom Behaviour Blocks */
    public static BlockExcitationCoil EXCITATION_COIL;
    public static BlockDust DUST;

    public static void preInit() {
        /* Dense Blocks */
        DENSE_MAGMA = createBlock(
                new BlockBase(makeLabsName("densemagma"), LabsCreativeTabs.TAB_NOMI_LABS, Material.ROCK,
                        SoundType.STONE),
                EnumRarity.COMMON, 64);
        DENSE_OIL_SHALE = createBlock(
                new BlockBase(makeLabsName("denseoilshale"), LabsCreativeTabs.TAB_NOMI_LABS, Material.ROCK,
                        SoundType.STONE),
                EnumRarity.COMMON, 64);

        /* Ultimate Blocks */
        ULTIMATE_POWER_STORAGE = createBlock(
                new BlockBase(makeLabsName("ultimate_power_storage"), LabsCreativeTabs.TAB_NOMI_LABS, Material.IRON,
                        SoundType.METAL, ToolClasses.PICKAXE, 2,
                        translatable("tooltip.nomilabs.general.crafting_component")),
                EnumRarity.EPIC, 1);
        ULTIMATE_GENERATOR = createBlock(
                new BlockBase(makeLabsName("ultimate_generator"), LabsCreativeTabs.TAB_NOMI_LABS, Material.IRON,
                        SoundType.METAL, ToolClasses.PICKAXE, 2,
                        translatable("tooltip.nomilabs.general.crafting_component")),
                EnumRarity.EPIC, 1);

        /* Misc Blocks */
        MICROVERSE_CASING = createBlock(new BlockBase(makeLabsName("microverse_casing"), LabsCreativeTabs.TAB_NOMI_LABS,
                Material.ROCK, SoundType.STONE, ToolClasses.WRENCH, 2), EnumRarity.COMMON, 64);

        /* Custom Behaviour Blocks */
        EXCITATION_COIL = createBlockWithItem(
                new BlockExcitationCoil(makeLabsName("excitationcoil"), LabsCreativeTabs.TAB_NOMI_LABS),
                ItemExcitationCoil::new);
        DUST = createBlock(new BlockDust(makeLabsName("block_dust"), LabsCreativeTabs.TAB_NOMI_LABS),
                EnumRarity.COMMON, 64);
    }

    public static void register(IForgeRegistry<Block> registry) {
        /* Register All Items in BLOCKS */
        for (Block block : BLOCKS) {
            registerBlock(block, registry);
        }
    }

    public static <T extends Block> T createBlock(T block, @NotNull IRarity rarity, int stackSize) {
        return createBlockWithItem(block, registeredBlock -> new ItemBlockBase(registeredBlock, rarity, stackSize));
    }

    public static <T extends Block> T createBlockWithItem(T block, Function<T, ItemBlock> itemBlockSupplier) {
        return createBlockWithRegisteredItem(block,
                (registeredBlock) -> LabsItems.createItem(itemBlockSupplier.apply(block)));
    }

    public static <T extends Block> T createBlockWithRegisteredItem(T block, Function<T, ItemBlock> itemBlockSupplier) {
        BLOCKS.add(block);
        ITEMS.put(block, itemBlockSupplier.apply(block));
        return block;
    }

    public static <T extends Block> T createBlockWithoutItem(T block) {
        BLOCKS.add(block);
        return block;
    }

    private static void registerBlock(Block block, IForgeRegistry<Block> registry) {
        registry.register(block);
        if (block.getTranslationKey().equals(nullTranslationKey)) {
            ResourceLocation rl = block.getRegistryName();
            assert rl != null;
            block.setTranslationKey(rl.getNamespace() + "." + rl.getPath());
        }
    }
}
