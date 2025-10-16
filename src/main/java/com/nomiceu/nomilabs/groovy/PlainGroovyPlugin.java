package com.nomiceu.nomilabs.groovy;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.groovyscript.api.GroovyPlugin;
import com.cleanroommc.groovyscript.compat.mods.GroovyContainer;

public class PlainGroovyPlugin implements GroovyPlugin {

    private final String modid;

    /**
     * GroovyScript uses this to auto-register, but use this NO WHERE ELSE.
     * Attempt to set modid to null so that the Loader.isModLoaded check fails, and the plugin is not registered.
     */
    @Deprecated
    public PlainGroovyPlugin() {
        this.modid = null;
    }

    public PlainGroovyPlugin(String modid) {
        this.modid = modid;
    }

    @Override
    public @NotNull String getModId() {
        // We have to return a null modid to try to get Loader.isModLoaded to fail
        // noinspection DataFlowIssue
        return modid;
    }

    @Override
    public @NotNull String getContainerName() {
        if (modid == null) {
            throw new IllegalStateException("Unused Plain Groovy Plugin registered! This may cause various issues!");
        }
        return "Nomi Labs disabled support for " + modid;
    }

    @Override
    public void onCompatLoaded(GroovyContainer<?> container) {
        if (modid == null) {
            throw new IllegalStateException("Unused Plain Groovy Plugin registered! This may cause various issues!");
        }
    }

    @Override
    public @NotNull Priority getOverridePriority() {
        return Priority.OVERRIDE_HIGHEST;
    }
}
