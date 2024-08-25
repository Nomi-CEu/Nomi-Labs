package com.nomiceu.nomilabs.integration.betterp2p;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import kotlin.Pair;

import java.util.List;

public class LabsClientCache {

    public static final List<Pair<BlockPos, EnumFacing>> inputLoc = new ObjectArrayList<>();
    public static final List<Pair<BlockPos, EnumFacing>> outputLoc = new ObjectArrayList<>();
}
