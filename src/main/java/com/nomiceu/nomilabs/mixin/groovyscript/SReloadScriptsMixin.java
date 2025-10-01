package com.nomiceu.nomilabs.mixin.groovyscript;

import net.minecraft.command.ICommandSender;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.cleanroommc.groovyscript.GroovyScript;
import com.cleanroommc.groovyscript.network.SReloadScripts;
import com.cleanroommc.groovyscript.sandbox.LoadStage;

/**
 * Fixes scripts not being reloaded on client.
 */
@Mixin(value = SReloadScripts.class, remap = false)
public class SReloadScriptsMixin {

    @Redirect(method = "executeClient",
              at = @At(value = "INVOKE",
                       target = "Lcom/cleanroommc/groovyscript/GroovyScript;postScriptRunResult(Lnet/minecraft/command/ICommandSender;ZZZJ)V"),
              require = 1)
    private void reloadScriptsOnClient(ICommandSender s, boolean i, boolean executing, boolean n, long timeOld) {
        // noinspection UnstableApiUsage
        long time = GroovyScript.runGroovyScriptsInLoader(LoadStage.POST_INIT);

        GroovyScript.postScriptRunResult(s, i, executing, n, time);
    }
}
