package com.nomiceu.nomilabs.mixin.jei;

import static mezz.jei.gui.overlay.IngredientGrid.INGREDIENT_PADDING;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.util.LabsTranslate;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mezz.jei.autocrafting.RecipeBookmarkGroup;
import mezz.jei.bookmarks.BookmarkGroup;
import mezz.jei.config.Config;
import mezz.jei.config.KeyBindings;
import mezz.jei.gui.TooltipRenderer;
import mezz.jei.gui.ingredients.IIngredientListElement;
import mezz.jei.gui.overlay.bookmarks.BookmarkGridWithNavigation;
import mezz.jei.gui.overlay.bookmarks.group.BookmarkGroupDisplay;
import mezz.jei.gui.overlay.bookmarks.group.BookmarkGroupOrganizer;
import mezz.jei.render.IngredientListBatchRenderer;
import mezz.jei.render.IngredientListSlot;

/**
 * Implements <a href="https://github.com/CleanroomMC/HadEnoughItems/pull/158">HEI #158</a>.
 */
@Mixin(value = BookmarkGroupOrganizer.class, remap = false)
public class BookmarkGroupOrganizerMixin {

    @Shadow
    private Rectangle area;

    @Shadow
    private int hoveredGroupId;

    @Shadow
    @Final
    private java.util.List<BookmarkGroupDisplay> groups;

    @Shadow
    private int missingIngredients;

    @Shadow
    @Final
    private IngredientListBatchRenderer missingIngredientRenderer;

    // Replace whole method, too complex to inject
    @Inject(method = "drawTooltips", at = @At("HEAD"), cancellable = true)
    private void newTooltipLogic(Minecraft minecraft, int mouseX, int mouseY, CallbackInfo ci) {
        ci.cancel();
        if (!Config.areRecipeBookmarksEnabled()) {
            return;
        }
        if (mouseX > area.x + BookmarkGridWithNavigation.BOOKMARK_TAB_WIDTH) {
            hoveredGroupId = -1;
            return;
        }

        boolean hovered = false;
        for (BookmarkGroupDisplay group : groups) {
            if (mouseY < group.getArea().y || mouseY > group.getArea().y + group.getArea().height) {
                continue;
            }
            List<String> tooltips = new ArrayList<>();
            List<IngredientListBatchRenderer> slotRows = new ArrayList<>();
            BookmarkGroup groupInternal = ((BookmarkGroupDisplayAccessor) group).labs$getGroup();

            if (groupInternal instanceof RecipeBookmarkGroup) {
                tooltips.add(LabsTranslate.translate("hei.tooltip.labs.group_organizer_mixin.recipe_group"));
            } else {
                tooltips.add(LabsTranslate.translate("hei.tooltip.labs.group_organizer_mixin.item_group"));
            }

            // Detect if the user is holding either ALT key.
            if (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU)) {
                tooltips.add(LabsTranslate.translate("hei.tooltip.labs.group_organizer_mixin.organizer.1"));
                tooltips.add(LabsTranslate.translate("hei.tooltip.labs.group_organizer_mixin.organizer.2"));
                if (groupInternal instanceof RecipeBookmarkGroup) {
                    tooltips.add(LabsTranslate.translate("hei.tooltip.labs.group_organizer_mixin.organizer.3"));
                    if (Config.isAutocraftingEnabled()) {
                        tooltips.add(LabsTranslate.translate("hei.tooltip.labs.group_organizer_mixin.organizer.4",
                                KeyBindings.crafting.getDisplayName()));
                    }
                }
            } else {
                hovered = true;
                tooltips.add(LabsTranslate.translate("hei.tooltip.labs.group_organizer_mixin.press_alt"));
                if (groupInternal instanceof RecipeBookmarkGroup recipeGroup) {
                    if (groupInternal.id != hoveredGroupId) {
                        // noinspection rawtypes
                        List<IIngredientListElement> missing = recipeGroup.getMissingIngredients();
                        missingIngredients = missing.size();
                        missingIngredientRenderer.clear();
                        List<IngredientListSlot> slots = new ObjectArrayList<>();

                        for (var ignored : missing) {
                            slots.add(new IngredientListSlot(0, 0, INGREDIENT_PADDING));
                        }

                        missingIngredientRenderer.add(slots);
                        missingIngredientRenderer.set(0, missing);
                    }
                    if (missingIngredients > 0) {
                        tooltips.add(LabsTranslate.translate("hei.tooltip.missing_ingredients"));
                        slotRows.add(missingIngredientRenderer);
                    }
                }
            }
            TooltipRenderer.drawHoveringTextAndItems(minecraft, tooltips, slotRows, mouseX, mouseY);
            break;
        }

        if (!hovered) {
            hoveredGroupId = -1;
        }
    }
}
