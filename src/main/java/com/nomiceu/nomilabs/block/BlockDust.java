package com.nomiceu.nomilabs.block;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;

import gregtech.api.items.toolitem.ToolClasses;

public class BlockDust extends BlockFalling {

    public BlockDust(ResourceLocation rl, CreativeTabs tab) {
        super(Material.SAND);
        setSoundType(SoundType.SAND);
        setCreativeTab(tab);
        setHardness(0.4F);
        setResistance(0.4F);
        setHarvestLevel(ToolClasses.SHOVEL, 0);
        setRegistryName(rl);
    }
}
