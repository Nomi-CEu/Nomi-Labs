package com.nomiceu.nomilabs.block.registry;

import com.nomiceu.nomilabs.block.*;
import com.nomiceu.nomilabs.item.ItemBlockBase;
import com.nomiceu.nomilabs.creativetab.registry.LabsCreativeTabs;
import com.nomiceu.nomilabs.item.ItemExcitationCoil;
import com.nomiceu.nomilabs.item.registry.LabsItems;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.nomiceu.nomilabs.util.LabsNames.makeCTName;

public class LabsBlocks {
    private static final String nullTranslationKey = "tile.null";
    private static final List<Block> BLOCKS = new ArrayList<>();

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
        DENSE_MAGMA = createBlock(new BlockBase(makeCTName("densemagma"), LabsCreativeTabs.TAB_NOMI_LABS, Material.ROCK, SoundType.STONE),
                EnumRarity.COMMON, 64);
        DENSE_OIL_SHALE = createBlock(new BlockBase(makeCTName("denseoilshale"), LabsCreativeTabs.TAB_NOMI_LABS, Material.ROCK, SoundType.STONE),
                EnumRarity.COMMON, 64);

        /* Ultimate Blocks */
        ULTIMATE_POWER_STORAGE = createBlock(new BlockBase(makeCTName("ultimate_power_storage"), LabsCreativeTabs.TAB_NOMI_LABS, Material.IRON, SoundType.METAL),
                EnumRarity.EPIC, 1);
        ULTIMATE_GENERATOR = createBlock(new BlockBase(makeCTName("ultimate_generator"), LabsCreativeTabs.TAB_NOMI_LABS, Material.IRON, SoundType.METAL),
                EnumRarity.EPIC, 1);

        /* Misc Blocks */
        MICROVERSE_CASING = createBlock(new BlockBase(makeCTName("microverse_casing"), LabsCreativeTabs.TAB_NOMI_LABS, Material.ROCK, SoundType.STONE),
                EnumRarity.COMMON, 64);

        /* Custom Behaviour Blocks */
        // Register Item separately to allow putting excitation coil on head
        EXCITATION_COIL = createBlockWithoutItem(new BlockExcitationCoil(makeCTName("excitationcoil"), LabsCreativeTabs.TAB_NOMI_LABS));
        LabsItems.createItem(new ItemExcitationCoil(EXCITATION_COIL));
        DUST = createBlock(new BlockDust(makeCTName("block_dust"), LabsCreativeTabs.TAB_NOMI_LABS),
                EnumRarity.COMMON, 64);
    }

    public static void register(IForgeRegistry<Block> registry) {
        /* Register All Items in BLOCKS */
        for (Block block : BLOCKS) {
            registerBlock(block, registry);
        }
    }

    public static <T extends Block> T createBlock(T block, @NotNull IRarity rarity, int stackSize) {
        BLOCKS.add(block);
        LabsItems.createItem(new ItemBlockBase(block, rarity, stackSize));
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
