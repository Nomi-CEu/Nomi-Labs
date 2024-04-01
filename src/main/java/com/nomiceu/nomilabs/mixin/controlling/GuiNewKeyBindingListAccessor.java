package com.nomiceu.nomilabs.mixin.controlling;

import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import us.getfluxed.controlsearch.client.gui.GuiNewKeyBindingList;

@Mixin(value = GuiNewKeyBindingList.class, remap = false)
public interface GuiNewKeyBindingListAccessor {
    @Accessor(value = "wrapped")
    static int getWrapped() {
        throw new NotImplementedException("GuiNewKeyBindingListAccessor failed to apply!");
    }

    @Accessor(value = "mc")
    Minecraft getMc();
}
