package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.List;

import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import com.cleanroommc.groovyscript.command.TextCopyable;
import com.llamalad7.mixinextras.sugar.Local;
import com.nomiceu.nomilabs.gregtech.mixinhelper.GroovyHandFixHelper;

import gregtech.api.unification.stack.MaterialStack;
import gregtech.integration.groovy.GroovyHandCommand;

/**
 * Applies <a href="https://github.com/GregTechCEu/GregTech/pull/2785">#2785</a>.
 */
@Mixin(value = GroovyHandCommand.class, remap = false)
public class GroovyHandCommandMixin {

    @Redirect(method = "onHandCommand",
              slice = @Slice(from = @At(value = "INVOKE",
                                        target = "Lgregtech/api/unification/OreDictUnifier;getMaterial(Lnet/minecraft/item/ItemStack;)Lgregtech/api/unification/stack/MaterialStack;"),
                             to = @At(value = "INVOKE",
                                      target = "Lgregtech/api/unification/OreDictUnifier;getPrefix(Lnet/minecraft/item/ItemStack;)Lgregtech/api/unification/ore/OrePrefix;")),
              at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"),
              require = 1)
    private static boolean newMaterialLogic(List<Object> instance, Object e,
                                            @Local MaterialStack material) {
        String materialString = GroovyHandFixHelper.getRlPrefix(material.material) + material.material;
        String copyText = "material('" + materialString + "')";
        instance.add(TextCopyable.translation(copyText, "gregtech.command.hand.material").build()
                .appendSibling(new TextComponentString(" " + materialString)
                        .setStyle(new Style().setColor(TextFormatting.GREEN))));
        return true;
    }
}
