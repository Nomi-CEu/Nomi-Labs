package com.nomiceu.nomilabs.mixin.vanilla;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.common.Loader;

import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.*;

import com.google.common.collect.ImmutableSet;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.groovy.mixinhelper.AccessibleKeyBinding;

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

    @Unique
    @Override
    public void labs$setDefaultKeyModifierAndCode(KeyModifier modifier, int keyCode) {
        keyCodeDefault = keyCode;
        keyModifierDefault = modifier;
        setKeyModifierAndCode(modifier, keyCode);

        // Key binding patch support
        if (Loader.isModLoaded(LabsValues.KEY_BINDING_PATCH_MODID)) {
            try {
                // noinspection JavaReflectionMemberAccess
                Field currentCmbKeys = KeyBinding.class.getDeclaredField("current_cmb_keys");
                // noinspection JavaReflectionMemberAccess
                Field defaultCmbKeys = KeyBinding.class.getDeclaredField("default_cmb_keys");

                ImmutableSet<Integer> cmbSet = switch (modifier) {
                    case CONTROL -> Minecraft.IS_RUNNING_ON_MAC ? ImmutableSet.of(Keyboard.KEY_LMETA) :
                            ImmutableSet.of(Keyboard.KEY_LCONTROL);
                    case SHIFT -> ImmutableSet.of(Keyboard.KEY_LSHIFT);
                    case ALT -> ImmutableSet.of(Keyboard.KEY_LMENU);
                    default -> ImmutableSet.of();
                };

                currentCmbKeys.set(this, cmbSet);
                defaultCmbKeys.set(this, cmbSet);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                NomiLabs.LOGGER.error("Failed to apply default key modifier change to key binding patch", e);
            }
        }
    }
}
