package com.nomiceu.nomilabs.integration.top;

import java.util.List;

import net.minecraft.block.state.IBlockState;

import org.jetbrains.annotations.Nullable;

public interface TOPInfoProvider {

    @Nullable
    List<String> getTOPMessage(IBlockState ignoredState);
}
