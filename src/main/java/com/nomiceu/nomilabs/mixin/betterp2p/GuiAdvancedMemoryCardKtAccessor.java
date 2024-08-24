package com.nomiceu.nomilabs.mixin.betterp2p;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.projecturanus.betterp2p.client.gui.GuiAdvancedMemoryCardKt;

/**
 * Allows Accessing Util Functions.
 */
@Mixin(value = GuiAdvancedMemoryCardKt.class, remap = false)
public interface GuiAdvancedMemoryCardKtAccessor {

    @Invoker("fmtTooltips")
    static List<String> fmtTooltips(String title, String[] keys, int maxChars) {
        throw new NotImplementedException("GuiAdvancedMemoryCardKt Accessor failed to apply!");
    }
}
