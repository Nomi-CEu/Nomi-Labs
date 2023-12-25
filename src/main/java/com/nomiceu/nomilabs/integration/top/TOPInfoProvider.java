package com.nomiceu.nomilabs.integration.top;

import net.minecraft.block.state.IBlockState;

import java.util.List;

public interface TOPInfoProvider {
    List<String> getTOPMessage(IBlockState state);
}
