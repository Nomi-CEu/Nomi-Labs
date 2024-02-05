package com.nomiceu.nomilabs.mixin.gregtech;

import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.api.IResourceStack;
import gregtech.api.unification.stack.MaterialStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

/**
 * Allows using the Groovy '*' Operator, and allows it to be treated as an IIngredient.
 */
@Mixin(value = MaterialStack.class, remap = false)
@SuppressWarnings("unused")
public abstract class MaterialStackMixin implements IIngredient {
    @Shadow
    @Final
    public long amount;

    @Shadow
    public abstract MaterialStack copy(long amount);

    @Unique
    public IResourceStack multiply(Number num) {
        return (IResourceStack) copy(num.longValue());
    }
}
