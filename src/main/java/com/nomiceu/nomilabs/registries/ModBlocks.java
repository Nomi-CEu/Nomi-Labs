package com.nomiceu.nomilabs.registries;

import com.nomiceu.nomilabs.block.BaseBlock;
import com.nomiceu.nomilabs.block.BaseItemBlock;
import com.nomiceu.nomilabs.block.BlockDust;
import com.nomiceu.nomilabs.block.BlockExcitationCoil;
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

import static com.nomiceu.nomilabs.util.RegistryNames.makeCTName;

public class ModBlocks {
    private static final String nullTranslationKey = "tile.null";
    private static final List<Block> BLOCKS = new ArrayList<>();

    /* Dense Blocks */
    public static BaseBlock DENSE_MAGMA;
    public static BaseBlock DENSE_OIL_SHALE;

    /* Ultimate Blocks */
    public static BaseBlock ULTIMATE_POWER_STORAGE;
    public static BaseBlock ULTIMATE_GENERATOR;

    /* Misc Blocks */
    public static BaseBlock MICROVERSE_CASING;

    /* Custom Behaviour Blocks */
    public static BlockExcitationCoil EXCITATION_COIL;
    public static BlockDust DUST;

    public static void preInit() {
        /* Dense Blocks */
        DENSE_MAGMA = createBlock(new BaseBlock(makeCTName("densemagma"), LabsCreativeTabs.TAB_NOMI_LABS, Material.ROCK, SoundType.STONE),
                EnumRarity.COMMON, 64);
        DENSE_OIL_SHALE = createBlock(new BaseBlock(makeCTName("denseoilshale"), LabsCreativeTabs.TAB_NOMI_LABS, Material.ROCK, SoundType.STONE),
                EnumRarity.COMMON, 64);

        /* Ultimate Blocks */
        ULTIMATE_POWER_STORAGE = createBlock(new BaseBlock(makeCTName("ultimate_power_storage"), LabsCreativeTabs.TAB_NOMI_LABS, Material.IRON, SoundType.METAL),
                EnumRarity.EPIC, 1);
        ULTIMATE_GENERATOR = createBlock(new BaseBlock(makeCTName("ultimate_generator"), LabsCreativeTabs.TAB_NOMI_LABS, Material.IRON, SoundType.METAL),
                EnumRarity.EPIC, 1);

        /* Misc Blocks */
        MICROVERSE_CASING = createBlock(new BaseBlock(makeCTName("microverse_casing"), LabsCreativeTabs.TAB_NOMI_LABS, Material.ROCK, SoundType.STONE),
                EnumRarity.COMMON, 64);

        /* Custom Behaviour Blocks */
        EXCITATION_COIL = createBlock(new BlockExcitationCoil(makeCTName("excitationcoil"), LabsCreativeTabs.TAB_NOMI_LABS),
                EnumRarity.UNCOMMON, 64);
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
        ModItems.createItem(new BaseItemBlock(block, rarity, stackSize));
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
