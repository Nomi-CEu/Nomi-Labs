package com.nomiceu.nomilabs.dimension;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.IChunkGenerator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LabsVoidWorldProvider extends WorldProvider {

    private static final BlockPos spawnPos = new BlockPos(2.5, 80, 2.5);

    @Override
    @NotNull
    public DimensionType getDimensionType() {
        return LabsDimensions.VOID;
    }

    @Override
    @NotNull
    public IChunkGenerator createChunkGenerator() {
        return new ChunkGeneratorFlat(world, getSeed(), false, "3;minecraft:air;1;decoration");
    }

    @Override
    @NotNull
    public BlockPos getRandomizedSpawnPoint() {
        return spawnPos;
    }

    @Override
    @NotNull
    public BlockPos getSpawnPoint() {
        return spawnPos;
    }

    @Override
    @Nullable
    public BlockPos getSpawnCoordinate() {
        return spawnPos;
    }
}
