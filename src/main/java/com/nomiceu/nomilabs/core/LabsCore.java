package com.nomiceu.nomilabs.core;

import java.util.List;
import java.util.Map;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;
import com.nomiceu.nomilabs.LabsValues;

import zone.rong.mixinbooter.IEarlyMixinLoader;

@IFMLLoadingPlugin.Name("LabsCore")
@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
@IFMLLoadingPlugin.TransformerExclusions("com.nomiceu.nomilabs.core.")
@IFMLLoadingPlugin.SortingIndex(-1001)
public class LabsCore implements IFMLLoadingPlugin, IEarlyMixinLoader {

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { "com.nomiceu.nomilabs.core.LabsTransformer" };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {}

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public List<String> getMixinConfigs() {
        return ImmutableList.of(
                "mixins." + LabsValues.LABS_MODID + ".json",
                "mixins." + LabsValues.LABS_MODID + "." + LabsValues.GROOVY_MODID + ".early.json");
    }
}
