package com.nomiceu.nomilabs.mixin.gregtech;

import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleMultiblockInfoRecipeWrapper;

import gregtech.integration.jei.multiblock.MultiblockInfoRecipeWrapper;
import mezz.jei.api.gui.IDrawable;

@Mixin(value = MultiblockInfoRecipeWrapper.class, remap = false)
public abstract class MultiblockInfoRecipeWrapperMixin implements AccessibleMultiblockInfoRecipeWrapper {

    @Shadow
    private IDrawable infoIcon;

    @Accessor("nextLayerButton")
    public abstract GuiButton getNextLayerButton();

    @Accessor("buttonPreviousPattern")
    public abstract GuiButton getButtonPreviousPattern();

    @Accessor("buttonNextPattern")
    public abstract GuiButton getButtonNextPattern();

    @Accessor("buttons")
    public abstract Map<GuiButton, Runnable> getButtons();

    @Invoker("switchRenderPage")
    @Override
    public abstract void callSwitchRenderPage(int amount);

    @Accessor("currentRendererPage")
    @Override
    public abstract int getCurrentRendererPage();

    @Unique
    public void setInfoIconToNone() {
        infoIcon = new IDrawable() {

            @Override
            public int getWidth() {
                return 0;
            }

            @Override
            public int getHeight() {
                return 0;
            }

            @Override
            public void draw(@NotNull Minecraft minecraft, int xOffset, int yOffset) {}
        };
    }

    @Accessor("drawInfoIcon")
    public abstract void setShouldDrawInfo(boolean shouldDrawInfo);
}
