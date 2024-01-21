package com.nomiceu.nomilabs.groovy;

import com.cleanroommc.groovyscript.api.GroovyPlugin;
import com.cleanroommc.groovyscript.compat.mods.GroovyContainer;
import com.nomiceu.nomilabs.LabsValues;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class LabsGroovyPlugin implements GroovyPlugin {
    @Override
    @NotNull
    public String getModId() {
        return LabsValues.LABS_MODID;
    }

    @Override
    public void onCompatLoaded(GroovyContainer<?> container) {
        container.getVirtualizedRegistrar().addFieldsOf(LabsVirtualizedRegistries.class);
    }
}
