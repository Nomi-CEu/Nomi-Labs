package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.Collections;
import java.util.function.Function;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.cleanroommc.groovyscript.compat.mods.GroovyContainer;
import com.cleanroommc.groovyscript.mapper.ObjectMapper;
import com.cleanroommc.groovyscript.mapper.TextureBinder;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.nomiceu.nomilabs.groovy.LabsGroovyPlugin;

import gregtech.api.unification.material.Material;
import gregtech.integration.groovy.GroovyScriptModule;

/**
 * Adds LSP Textual Hint Inlays.
 */
@SuppressWarnings("UnstableApiUsage")
@Mixin(value = GroovyScriptModule.class, remap = false)
public class GroovyScriptModuleMixin {

    @WrapOperation(method = "onCompatLoaded",
                   at = @At(value = "INVOKE",
                            target = "Lcom/cleanroommc/groovyscript/compat/mods/GroovyContainer;objectMapperBuilder(Ljava/lang/String;Ljava/lang/Class;)Lcom/cleanroommc/groovyscript/mapper/ObjectMapper$Builder;",
                            ordinal = 1))
    private ObjectMapper.Builder<Material> materialTextures(GroovyContainer<?> instance, String name,
                                                            Class<Material> returnType,
                                                            Operation<ObjectMapper.Builder<Material>> original) {
        return original.call(instance, name, returnType)
                .textureBinder(LabsGroovyPlugin.textureBinderMaterial(Function.identity()))
                .tooltip(it -> Collections.singletonList(it.getLocalizedName()));
    }

    @WrapOperation(method = "onCompatLoaded",
                   at = @At(value = "INVOKE",
                            target = "Lcom/cleanroommc/groovyscript/compat/mods/GroovyContainer;objectMapperBuilder(Ljava/lang/String;Ljava/lang/Class;)Lcom/cleanroommc/groovyscript/mapper/ObjectMapper$Builder;",
                            ordinal = 3))
    private ObjectMapper.Builder<ItemStack> metaItemTextures(GroovyContainer<?> instance, String name,
                                                             Class<ItemStack> returnType,
                                                             Operation<ObjectMapper.Builder<ItemStack>> original) {
        return original.call(instance, name, returnType)
                .textureBinder(TextureBinder.ofItem())
                .tooltip(it -> Collections.singletonList(it.getDisplayName()));
    }
}
