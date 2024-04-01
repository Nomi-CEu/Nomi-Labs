package com.nomiceu.nomilabs.mixin;

import com.nomiceu.nomilabs.groovy.mixinhelper.AccessibleKeyBinding;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyModifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Allows Setting of Default Key Codes and Modifies.
 */
@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin implements AccessibleKeyBinding {
    @Shadow
    @Final
    @Mutable
    private int keyCodeDefault;

    @Shadow
    private KeyModifier keyModifierDefault;

    @Shadow
    public abstract void setKeyModifierAndCode(KeyModifier keyModifier, int keyCode);

    @Override
    public void setDefaultKeyModifierAndCode(KeyModifier modifier, int keyCode) {
        keyCodeDefault = keyCode;
        keyModifierDefault = modifier;
        setKeyModifierAndCode(modifier, keyCode);
    }
}
