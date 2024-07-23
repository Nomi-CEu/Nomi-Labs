package com.nomiceu.nomilabs.mixin.groovyscript;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.sandbox.GroovySandbox;
import com.cleanroommc.groovyscript.sandbox.GroovyScriptSandbox;

import groovy.lang.Closure;

/**
 * Temp Fix for Groovy Sandbox Running Detection.
 */
@Mixin(value = GroovyScriptSandbox.class, remap = false)
public abstract class GroovyScriptSandboxMixin extends GroovySandbox {

    @Shadow
    private static <T> T runClosureInternal(Closure<T> closure, Object[] args) {
        return null;
    }

    @Shadow
    @Final
    private Map<List<StackTraceElement>, AtomicInteger> storedExceptions;

    protected GroovyScriptSandboxMixin(URL[] scriptEnvironment) {
        super(scriptEnvironment);
    }

    @Inject(method = "runClosure", at = @At("HEAD"), cancellable = true)
    private <T> void setRunningProperlyClosure(Closure<T> closure, Object[] args, CallbackInfoReturnable<T> cir) {
        if (!isRunning()) startRunning();
        T result = null;
        try {
            result = runClosureInternal(closure, args);
        } catch (Throwable t) {
            storedExceptions.computeIfAbsent(Arrays.asList(t.getStackTrace()), k -> {
                GroovyLog.get().error("An exception occurred while running a closure!");
                GroovyLog.get().exception(t);
                return new AtomicInteger();
            }).addAndGet(1);
        } finally {
            if (!isRunning()) stopRunning();
        }
        cir.setReturnValue(result);
    }
}
