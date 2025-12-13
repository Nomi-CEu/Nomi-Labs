package com.nomiceu.nomilabs.mixin.groovyscript;

import net.minecraft.client.network.NetHandlerPlayClient;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cleanroommc.groovyscript.GroovyScript;
import com.cleanroommc.groovyscript.network.IPacket;
import com.cleanroommc.groovyscript.network.SReloadScripts;
import com.cleanroommc.groovyscript.sandbox.LoadStage;

/**
 * Fixes scripts not being reloaded on client.
 */
@Mixin(value = SReloadScripts.class, remap = false)
public class SReloadScriptsMixin {

    @Shadow
    private boolean changePackmode;

    @Shadow
    private String packmode;

    @Inject(method = "executeClient",
            at = @At("HEAD"),
            require = 1)
    private void reloadScriptsOnClient(NetHandlerPlayClient handler, CallbackInfoReturnable<IPacket> cir) {
        // If packmode was changed, that already triggers a script run
        if (!changePackmode || packmode == null) {
            // noinspection UnstableApiUsage
            GroovyScript.runGroovyScriptsInLoader(LoadStage.POST_INIT);
        }
    }
}
