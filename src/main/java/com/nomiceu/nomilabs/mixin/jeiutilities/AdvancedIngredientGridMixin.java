package com.nomiceu.nomilabs.mixin.jeiutilities;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.github.vfyjxf.jeiutilities.gui.history.AdvancedIngredientGrid;

import mezz.jei.gui.ingredients.IIngredientListElement;
import mezz.jei.gui.overlay.GridAlignment;
import mezz.jei.gui.overlay.IngredientGrid;
import mezz.jei.input.MouseHelper;
import mezz.jei.render.IngredientListBatchRenderer;
import mezz.jei.render.IngredientRenderer;

/**
 * Fixes ghost ingredient dragging from jei utils' history page.
 */
@Mixin(value = AdvancedIngredientGrid.class, remap = false)
public class AdvancedIngredientGridMixin extends IngredientGrid {

    @Shadow
    private boolean showHistory;

    @Shadow
    @Final
    private IngredientListBatchRenderer guiHistoryIngredientSlots;

    /**
     * Mandatory ignored constructor
     */
    private AdvancedIngredientGridMixin(IngredientListBatchRenderer guiIngredientSlots,
                                        GridAlignment alignment) {
        super(guiIngredientSlots, alignment);
    }

    @Override
    public @Nullable IIngredientListElement<?> getElementUnderMouse() {
        IIngredientListElement<?> under = super.getElementUnderMouse();

        if (under != null) return under;

        if (!showHistory) return null;

        IngredientRenderer<?> underHistory = guiHistoryIngredientSlots.getHovered(MouseHelper.getX(),
                MouseHelper.getY());
        if (underHistory != null)
            return underHistory.getElement();

        return null;
    }
}
