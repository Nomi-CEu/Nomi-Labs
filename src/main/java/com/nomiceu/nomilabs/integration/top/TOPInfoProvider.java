package com.nomiceu.nomilabs.integration.top;

import java.util.List;

import net.minecraft.block.state.IBlockState;

public interface TOPInfoProvider {

    List<String> getTOPMessage(IBlockState ignoredState);
}
