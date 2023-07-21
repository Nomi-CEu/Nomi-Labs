package com.nomiceu.nomilabs.block;


import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;

public class BlockBase extends Block {
    public BlockBase(ResourceLocation rl, CreativeTabs tab, Material material, SoundType sound) {
        super(material);
        setRegistryName(rl);
        setHardness(2.0F);
        setResistance(10.0F);
        setSoundType(sound);
        setCreativeTab(tab);
    }
}
