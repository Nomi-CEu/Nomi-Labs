package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.Set;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleMaterialFlags;

import gregtech.api.unification.material.info.MaterialFlag;
import gregtech.api.unification.material.info.MaterialFlags;

/**
 * Allows removing flags.
 */
@Mixin(value = MaterialFlags.class, remap = false)
public class MaterialFlagsMixin implements AccessibleMaterialFlags {

    @Shadow
    @Final
    private Set<MaterialFlag> flags;

    @Override
    @Unique
    public void removeFlags(MaterialFlag... toRemove) {
        for (var flag : toRemove) {
            flags.remove(flag);
        }
    }
}
