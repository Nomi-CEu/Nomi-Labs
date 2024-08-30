package com.nomiceu.nomilabs.mixin.vanilla;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiKeyBindingList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiKeyBindingList.class)
public interface GuiKeyBindingListAccessor {

    @Accessor(value = "mc")
    Minecraft getMc();
}
