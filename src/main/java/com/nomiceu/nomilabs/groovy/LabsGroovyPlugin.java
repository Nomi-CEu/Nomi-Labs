package com.nomiceu.nomilabs.groovy;

import java.util.Collections;
import java.util.function.Function;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.groovyscript.api.GroovyPlugin;
import com.cleanroommc.groovyscript.api.IObjectParser;
import com.cleanroommc.groovyscript.compat.mods.GroovyContainer;
import com.cleanroommc.groovyscript.compat.mods.ModSupport;
import com.cleanroommc.groovyscript.mapper.TextureBinder;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.mixin.earlygroovy.ModSupportAccessor;

import gregtech.api.GregTechAPI;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.stack.MaterialStack;

@SuppressWarnings({ "unused", "UnstableApiUsage" })
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

    public static void onConstruction() {
        for (String container : LabsConfig.advanced.disabledGrSContainers) {
            NomiLabs.LOGGER.info("Disabling GroovyScript Container for mod {}", container);
            ((ModSupportAccessor) ModSupport.INSTANCE).registerContainerOverride(new PlainGroovyPlugin(container));
        }
    }

    @Override
    public void onCompatLoaded(GroovyContainer<?> container) {
        container.objectMapperBuilder("materialstack", MaterialStack.class)
                .mod(LabsValues.LABS_MODID)
                .parser(IObjectParser
                        .wrapStringGetter((str) -> new MaterialStack(GregTechAPI.materialManager.getMaterial(str), 1)))
                .completerOfNamed(GregTechAPI.materialManager::getRegisteredMaterials, Material::getRegistryName)
                .textureBinder(textureBinderMaterial(stack -> stack.material))
                .tooltip(it -> Collections.singletonList(it.material.getLocalizedName()))
                .register();

        container.addProperty(LabsVirtualizedRegistries.REPLACE_RECYCLING_MANAGER);
        container.addProperty(LabsVirtualizedRegistries.REPLACE_DECOMP_MANAGER);
        container.addProperty(LabsVirtualizedRegistries.KEYBIND_OVERRIDES_MANAGER);
    }

    public static <T> TextureBinder<T> textureBinderMaterial(Function<T, Material> toMaterial) {
        return TextureBinder.of(toMaterial, material -> {
            ItemStack ingot = OreDictUnifier.get(OrePrefix.ingot, material);
            if (!ingot.isEmpty()) {
                TextureBinder.ofItem().bindTexture(ingot);
                return;
            }

            ItemStack dust = OreDictUnifier.get(OrePrefix.dust, material);
            if (!dust.isEmpty()) {
                TextureBinder.ofItem().bindTexture(dust);
                return;
            }

            if (material.hasFluid()) {
                Fluid fluid = material.getFluid();
                if (fluid != null) {
                    TextureBinder.ofFluid().bindTexture(new FluidStack(fluid, 1000));
                }
            }

            // Don't render texture
        });
    }
}
