package com.nomiceu.nomilabs.mixin;

import java.util.Map;

import net.minecraft.client.settings.KeyBinding;

import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBinding.class)
public interface KeyBindingAccessor {

    @Accessor(value = "KEYBIND_ARRAY")
    static Map<String, KeyBinding> getKeybindRegistry() {
        throw new NotImplementedException("KeyBindingAccessor Failed to Apply!");
    }
}
