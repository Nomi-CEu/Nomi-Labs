package com.nomiceu.nomilabs.integration.top;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
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
import mcjty.theoneprobe.apiimpl.elements.ElementItemStack;
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

        // Invalid machines, ignore
        if (!recipe.labs$isValidForOutputTop()) return;

        var itemFluidLists = createItemFluidElementLists(recipe);
        var items = itemFluidLists.getLeft();
        var fluids = itemFluidLists.getRight();

        if (items.isEmpty() && fluids.isEmpty()) return;

        boolean showDetailed = items.size() + fluids.size() <= Config.showItemDetailThresshold &&
                player.isSneaking();
        IProbeInfo mainPanel = info.vertical()
                .text(LabsTranslate.topTranslate("nomilabs.top.recipe_outputs"))
                .vertical(info.defaultLayoutStyle().borderColor(Config.chestContentsBorderColor)
                        .spacing(5));

        if (showDetailed) {
            for (var entry : items) {
                mainPanel.horizontal(new LayoutStyle().spacing(10).alignment(ElementAlignment.ALIGN_CENTER))
                        .element(entry.getValue())
                        .text(TextStyleClass.INFO + entry.getKey());
            }

            for (var entry : fluids) {
                mainPanel.horizontal(new LayoutStyle().spacing(10).alignment(ElementAlignment.ALIGN_CENTER))
                        .element(entry.getValue())
                        .element(entry.getKey());
            }
            return;
        }

        // If outputs and fluid outputs are both of size 1, show on same row instead of over two rows
        boolean condense = items.size() == 1 && fluids.size() == 1;
        IProbeInfo sharedHorizontal = null;

        if (condense)
            sharedHorizontal = createHorizontalLayout(mainPanel);

        if (!items.isEmpty()) {
            IProbeInfo panel;
            if (condense)
                panel = sharedHorizontal;
            else
                panel = createHorizontalLayout(mainPanel);

            addOutputs(items, panel, Pair::getValue);
        }

        if (!fluids.isEmpty()) {
            IProbeInfo panel;
            if (condense)
                panel = sharedHorizontal;
            else
                panel = createHorizontalLayout(mainPanel);

            addOutputs(fluids, panel, Pair::getValue);
        }
    }

    private <T> void addOutputs(List<T> list, IProbeInfo panel, Function<T, IElement> getElement) {
        int idx = 0;

        for (var entry : list) {
            if (idx >= AMT_IN_ROW) break;

            panel.element(getElement.apply(entry));
            idx++;
        }
    }

    private IProbeInfo createHorizontalLayout(IProbeInfo mainPanel) {
        return mainPanel.horizontal(new LayoutStyle().spacing(4));
    }

    private Pair<List<Pair<String, ElementItemStack>>, List<Pair<LabsFluidNameElement, LabsFluidStackElement>>> createItemFluidElementLists(AccessibleAbstractRecipeLogic recipe) {
        // Items
        var outputs = getUnique(recipe.labs$getOutputs().subList(0, recipe.labs$getNonChancedItemAmt()),
                ItemStack::isEmpty, ItemMeta::new, ItemStack::getCount);

        var chancedOutputs = getUnique(recipe.labs$getChancedItemOutputs(),
                (chanced) -> chanced.getKey().isEmpty() || chanced.getValue() == 0,
                (chanced) -> Pair.of(new ItemMeta(chanced.getKey()), chanced.getValue()),
                (chanced) -> chanced.getKey().getCount());

        IItemStyle style = new ItemStyle().width(16).height(16);
        List<Pair<String, ElementItemStack>> items = new ArrayList<>();

        for (var output : outputs.entrySet()) {
            ItemStack stack = output.getKey().toStack(output.getValue());
            items.add(Pair.of(stack.getDisplayName(), new ElementItemStack(stack, style)));
        }

        for (var chanced : chancedOutputs.entrySet()) {
            ItemStack stack = chanced.getKey().getKey().toStack(chanced.getValue());
            String display = stack.getDisplayName() + " (" + LabsTOPUtils.formatChance(chanced.getKey().getValue()) +
                    ")";
            items.add(Pair.of(display, new LabsChancedItemStackElement(stack, chanced.getKey().getValue(), style)));
        }

        // Fluids
        var fluidOutputs = getUnique(recipe.labs$getFluidOutputs().subList(0, recipe.labs$getNonChancedFluidAmt()),
                (stack) -> stack.amount == 0, FluidStack::getFluid, (stack) -> stack.amount);

        var chancedFluidOutputs = getUnique(recipe.labs$getChancedFluidOutputs(),
                (chanced) -> chanced.getKey().amount == 0 || chanced.getValue() == 0,
                (chanced) -> Pair.of(chanced.getKey().getFluid(), chanced.getValue()),
                (chanced) -> chanced.getKey().amount);

        List<Pair<LabsFluidNameElement, LabsFluidStackElement>> fluids = new ArrayList<>();

        for (var output : fluidOutputs.entrySet()) {
            FluidStack stack = new FluidStack(output.getKey(), output.getValue());
            fluids.add(Pair.of(new LabsFluidNameElement(stack, null), new LabsFluidStackElement(stack)));
        }

        for (var chanced : chancedFluidOutputs.entrySet()) {
            FluidStack stack = new FluidStack(chanced.getKey().getKey(), chanced.getValue());
            fluids.add(Pair.of(new LabsChancedFluidNameElement(stack, chanced.getKey().getValue(), null),
                    new LabsChancedFluidStackElement(stack, chanced.getKey().getValue())));
        }
        return Pair.of(items, fluids);
    }

    private <T, K> Map<K, Integer> getUnique(List<T> stacks, Function<T, Boolean> emptyCheck, Function<T, K> getKey,
                                             Function<T, Integer> getCount) {
        Map<K, Integer> map = new Object2ObjectLinkedOpenHashMap<>();

        for (T stack : stacks) {
            if (emptyCheck.apply(stack)) continue;

            map.compute(getKey.apply(stack), (key, count) -> {
                if (count == null) count = 0;
                return count + getCount.apply(stack);
            });
        }

        return map;
    }
}
