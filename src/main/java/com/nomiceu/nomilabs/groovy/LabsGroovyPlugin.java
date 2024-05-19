package com.nomiceu.nomilabs.groovy;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.groovyscript.api.GroovyPlugin;
import com.cleanroommc.groovyscript.api.IGameObjectParser;
import com.cleanroommc.groovyscript.compat.mods.GroovyContainer;
import com.cleanroommc.groovyscript.gameobjects.GameObjectHandler;
import com.nomiceu.nomilabs.LabsValues;

import gregtech.api.GregTechAPI;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.stack.MaterialStack;

@SuppressWarnings("unused")
public class LabsGroovyPlugin implements GroovyPlugin {

    @Override
    public @NotNull String getContainerName() {
        return LabsValues.LABS_MODNAME;
    }

    @Override
    @NotNull
    public String getModId() {
        return LabsValues.LABS_MODID;
    }

    @Override
    public void onCompatLoaded(GroovyContainer<?> container) {
        GameObjectHandler.builder("materialstack", MaterialStack.class)
                .mod(LabsValues.LABS_MODID)
                .parser(IGameObjectParser
                        .wrapStringGetter((str) -> new MaterialStack(GregTechAPI.materialManager.getMaterial(str), 1)))
                .completerOfNamed(GregTechAPI.materialManager::getRegisteredMaterials, Material::getRegistryName)
                .register();

        container.getRegistrar().addRegistry(LabsVirtualizedRegistries.REPLACE_RECYCLING_MANAGER);
        container.getRegistrar().addRegistry(LabsVirtualizedRegistries.REPLACE_DECOMP_MANAGER);
    }
}
