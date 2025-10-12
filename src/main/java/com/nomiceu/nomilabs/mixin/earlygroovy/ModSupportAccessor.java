package com.nomiceu.nomilabs.mixin.earlygroovy;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.cleanroommc.groovyscript.api.GroovyPlugin;
import com.cleanroommc.groovyscript.compat.mods.ModSupport;

@Mixin(value = ModSupport.class, remap = false)
public interface ModSupportAccessor {

    @Invoker("registerContainer")
    void registerContainerOverride(GroovyPlugin container);
}
