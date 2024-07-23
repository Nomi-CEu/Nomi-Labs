package com.nomiceu.nomilabs.mixin.groovyscript;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cleanroommc.groovyscript.GroovyScript;
import com.cleanroommc.groovyscript.sandbox.GroovySandbox;

import groovy.lang.Closure;

/**
 * Temp Fix for Groovy Sandbox Running Detection.
 */
@Mixin(value = GroovySandbox.class, remap = false)
public abstract class GroovySandboxMixin {

    @Shadow
    protected abstract void stopRunning();

    @Shadow
    protected abstract void startRunning();

    @Shadow
    public abstract boolean isRunning();

    @Inject(method = "runClosure", at = @At("HEAD"), cancellable = true)
    private <T> void setClosureRunning(Closure<T> closure, Object[] args, CallbackInfoReturnable<T> cir) {
        if (!isRunning()) startRunning();
        T result = null;
        try {
            result = closure.call(args);
        } catch (Exception e) {
            GroovyScript.LOGGER.error("Caught an exception trying to run a closure:");
            e.printStackTrace();
        } finally {
            if (!isRunning()) stopRunning();
        }
        cir.setReturnValue(result);
    }
}
