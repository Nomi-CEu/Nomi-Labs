package com.nomiceu.nomilabs.integration.top;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleAbstractRecipeLogic;
import com.nomiceu.nomilabs.util.ItemMeta;
import com.nomiceu.nomilabs.util.LabsTranslate;

import gregtech.api.capability.GregtechTileCapabilities;
import gregtech.api.capability.IWorkable;
import gregtech.integration.theoneprobe.provider.CapabilityInfoProvider;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcjty.theoneprobe.config.Config;

public class RecipeOutputsProvider extends CapabilityInfoProvider<IWorkable> {

    private static final int AMT_IN_ROW = 10;

    @Override
    public String getID() {
        return LabsValues.LABS_MODID + ":recipe_outputs";
    }

    @Override
    protected @NotNull Capability<IWorkable> getCapability() {
        return GregtechTileCapabilities.CAPABILITY_WORKABLE;
    }

    @Override
    protected void addProbeInfo(IWorkable capability, IProbeInfo info, EntityPlayer player, TileEntity tile,
                                IProbeHitData data) {
        if (!LabsConfig.topSettings.enableGTRecipeOutput) return;

        if (capability.getProgress() <= 0 || (!(capability instanceof AccessibleAbstractRecipeLogic recipe))) return;

        // Generators, Ignore
        if (recipe.labs$getEUt() < 0) return;

        var outputs = getUniqueItems(recipe.labs$getOutputs());
        var fluidOutputs = getUniqueFluids(recipe.labs$getFluidOutputs());

        if (outputs.isEmpty() && fluidOutputs.isEmpty()) return;

        boolean showDetailed = outputs.size() + fluidOutputs.size() <= Config.showItemDetailThresshold &&
                player.isSneaking();
        IProbeInfo mainPanel = info.vertical()
                .text(LabsTranslate.topTranslate("nomilabs.top.recipe_outputs"))
                .vertical(info.defaultLayoutStyle().borderColor(Config.chestContentsBorderColor)
                        .spacing(5));

        if (showDetailed) {
            for (var entry : outputs.entrySet()) {
                ItemStack stack = entry.getKey().toStack(entry.getValue());
                mainPanel.horizontal(new LayoutStyle().spacing(10).alignment(ElementAlignment.ALIGN_CENTER))
                        .item(stack, new ItemStyle().width(16).height(16))
                        .text(TextStyleClass.INFO + stack.getDisplayName());
            }

            for (var entry : fluidOutputs.entrySet()) {
                FluidStack stack = new FluidStack(entry.getKey(), entry.getValue());
                mainPanel.horizontal(new LayoutStyle().spacing(10).alignment(ElementAlignment.ALIGN_CENTER))
                        .element(new LabsFluidStackElement(stack))
                        .element(new LabsFluidNameElement(stack, false));
            }
            return;
        }

        // If outputs and fluid outputs are both of size 1, show on same row instead of over two rows
        boolean condense = outputs.size() == 1 && fluidOutputs.size() == 1;
        IProbeInfo sharedHorizontal = null;

        if (condense)
            sharedHorizontal = createHorizontalLayout(mainPanel);

        if (!outputs.isEmpty()) {
            IProbeInfo panel;
            if (condense)
                panel = sharedHorizontal;
            else
                panel = createHorizontalLayout(mainPanel);

            addOutputs(outputs, (meta, amt) -> panel.item(meta.toStack(amt), new ItemStyle().width(16).height(16)));
        }

        if (!fluidOutputs.isEmpty()) {
            IProbeInfo panel;
            if (condense)
                panel = sharedHorizontal;
            else
                panel = createHorizontalLayout(mainPanel);

            addOutputs(fluidOutputs,
                    (fluid, amount) -> panel.element(new LabsFluidStackElement(new FluidStack(fluid, amount))));
        }
    }

    private <T> void addOutputs(Map<T, Integer> outputs, BiConsumer<T, Integer> addToPanel) {
        int idx = 0;

        for (var output : outputs.entrySet()) {
            if (idx >= AMT_IN_ROW) break;

            addToPanel.accept(output.getKey(), output.getValue());
            idx++;
        }
    }

    private IProbeInfo createHorizontalLayout(IProbeInfo mainPanel) {
        return mainPanel.horizontal(new LayoutStyle().spacing(4));
    }

    private Map<ItemMeta, Integer> getUniqueItems(List<ItemStack> stacks) {
        Map<ItemMeta, Integer> map = new Object2ObjectLinkedOpenHashMap<>();

        for (var stack : stacks) {
            if (stack.isEmpty()) continue;

            map.compute(new ItemMeta(stack), (meta, count) -> {
                if (count == null) count = 0;
                return count + stack.getCount();
            });
        }

        return map;
    }

    private Map<Fluid, Integer> getUniqueFluids(List<FluidStack> stacks) {
        Map<Fluid, Integer> map = new Object2ObjectLinkedOpenHashMap<>();

        for (var stack : stacks) {
            if (stack.amount == 0) continue;

            map.compute(stack.getFluid(), (meta, amount) -> {
                if (amount == null) amount = 0;
                return amount + stack.amount;
            });
        }

        return map;
    }
}
