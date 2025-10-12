package com.nomiceu.nomilabs.groovy;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.groovyscript.api.GroovyPlugin;
import com.cleanroommc.groovyscript.compat.mods.GroovyContainer;

public class PlainGroovyPlugin implements GroovyPlugin {

    private final String modid;

    public PlainGroovyPlugin(String modid) {
        this.modid = modid;
    }

    @Override
    public @NotNull String getModId() {
        return modid;
    }

    @Override
    public @NotNull String getContainerName() {
        return "Nomi Labs disabled support for " + modid;
    }

    @Override
    public void onCompatLoaded(GroovyContainer<?> container) {}

    @Override
    public @NotNull Priority getOverridePriority() {
        return Priority.OVERRIDE_HIGHEST;
    }
}
