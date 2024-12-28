package com.nomiceu.nomilabs.integration.betterp2p;

import java.util.List;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import kotlin.Pair;

public class LabsClientCache {

    public static final List<Pair<BlockPos, EnumFacing>> inputLoc = new ObjectArrayList<>();
    public static final List<Pair<BlockPos, EnumFacing>> outputLoc = new ObjectArrayList<>();
    public static SortModes sortMode = SortModes.DEFAULT;
}
