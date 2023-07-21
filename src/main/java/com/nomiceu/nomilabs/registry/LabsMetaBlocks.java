package com.nomiceu.nomilabs.registry;

import com.nomiceu.nomilabs.block.BlockUniqueCasing;
import com.nomiceu.nomilabs.item.ItemBlockBase;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import static com.nomiceu.nomilabs.util.RegistryNames.makeLabsName;

public class LabsMetaBlocks {
    public static BlockUniqueCasing UNIQUE_CASING;
    public static void preInit() {
        UNIQUE_CASING = new BlockUniqueCasing(makeLabsName("unique_casing"));
    }

    public static void registerItems(IForgeRegistry<Item> registry) {
        registry.register(new ItemBlockBase(UNIQUE_CASING, EnumRarity.COMMON, 64));
    }

    public static void registerBlocks(IForgeRegistry<Block> registry) {
        registry.register(UNIQUE_CASING);
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels() {
        UNIQUE_CASING.onModelRegister();
    }
}
