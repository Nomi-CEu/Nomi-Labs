package com.nomiceu.nomilabs.gregtech.block;

import javax.annotation.Nonnull;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import org.jetbrains.annotations.NotNull;

import gregtech.api.block.VariantActiveBlock;
import gregtech.common.ConfigHolder;

public class BlockUniqueCasing extends VariantActiveBlock<BlockUniqueCasing.UniqueCasingType> {

    public BlockUniqueCasing(ResourceLocation rl) {
        super(Material.IRON);
        setRegistryName(rl);
        setTranslationKey("unique_casing");
        setHardness(5.0f);
        setResistance(10.0f);
        setSoundType(SoundType.METAL);
        setHarvestLevel("wrench", 2);
        setDefaultState(getState(UniqueCasingType.GROWTH_LIGHT));
    }

    @Override
    public boolean canCreatureSpawn(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos,
                                    @Nonnull EntityLiving.SpawnPlacementType type) {
        return false;
    }

    @Override
    protected boolean isBloomEnabled(UniqueCasingType value) {
        return ConfigHolder.client.machinesEmissiveTextures;
    }

    @Nonnull
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public enum UniqueCasingType implements IStringSerializable {

        GROWTH_LIGHT("growth_light"),
        AIR_VENT("air_vent");

        private final String name;

        UniqueCasingType(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getName() {
            return name;
        }
    }
}
