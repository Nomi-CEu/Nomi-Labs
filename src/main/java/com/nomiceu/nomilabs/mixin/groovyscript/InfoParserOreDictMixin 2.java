package com.nomiceu.nomilabs.mixin.groovyscript;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cleanroommc.groovyscript.compat.vanilla.command.infoparser.InfoParserOreDict;
import com.cleanroommc.groovyscript.helper.ingredient.GroovyScriptCodeConverter;

/**
 * Fixes Weird Ore Dict Copy Text.
 */
@Mixin(value = InfoParserOreDict.class, remap = false)
public class InfoParserOreDictMixin {

    @Inject(method = "text(Ljava/lang/String;ZZ)Ljava/lang/String;", at = @At("HEAD"), cancellable = true)
    private void returnCodeAlways(String entry, boolean colored, boolean prettyNbt,
                                  CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(GroovyScriptCodeConverter.asGroovyCode(entry, colored));
    }
}
