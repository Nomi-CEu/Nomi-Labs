package com.nomiceu.nomilabs.groovy;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.groovyscript.api.GroovyPlugin;
import com.cleanroommc.groovyscript.api.IGameObjectHandler;
import com.cleanroommc.groovyscript.compat.mods.GroovyContainer;
import com.cleanroommc.groovyscript.gameobjects.GameObjectHandlerManager;
import com.nomiceu.nomilabs.LabsValues;

import gregtech.api.GregTechAPI;
import gregtech.api.unification.stack.MaterialStack;

@SuppressWarnings("unused")
public class LabsGroovyPlugin implements GroovyPlugin {

    @Override
    @NotNull
    public String getModId() {
        return LabsValues.LABS_MODID;
    }

    @Override
    public void onCompatLoaded(GroovyContainer<?> container) {
        GameObjectHandlerManager.registerGameObjectHandler(LabsValues.LABS_MODID, "materialstack",
                IGameObjectHandler
                        .wrapStringGetter((str) -> new MaterialStack(GregTechAPI.materialManager.getMaterial(str), 1)));
        container.getVirtualizedRegistrar().addFieldsOf(LabsVirtualizedRegistries.class);
    }
}
