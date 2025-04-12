package com.nomiceu.nomilabs.gregtech.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.util.LabsGroovyHelper;

import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.ingredients.GTRecipeItemInput;
import gregtech.api.recipes.ingredients.nbtmatch.NBTCondition;
import gregtech.api.recipes.ingredients.nbtmatch.NBTMatcher;
import gregtech.api.recipes.ingredients.nbtmatch.NBTTagType;
import gregtech.api.util.EnumValidationResult;
import mustapelto.deepmoblearning.common.metadata.MetadataManager;
import mustapelto.deepmoblearning.common.util.DataModelHelper;

public class DMESimChamberRecipeMapBuilder extends RecipeBuilder<DMESimChamberRecipeMapBuilder> {

    private boolean setDataItem;

    public DMESimChamberRecipeMapBuilder() {}

    @SuppressWarnings("unused")
    public DMESimChamberRecipeMapBuilder(Recipe recipe, RecipeMap<DMESimChamberRecipeMapBuilder> recipeMap) {
        super(recipe, recipeMap);
        this.setDataItem = false;
    }

    public DMESimChamberRecipeMapBuilder(@NotNull DMESimChamberRecipeMapBuilder builder) {
        super(builder);
        this.setDataItem = builder.setDataItem;
    }

    @Override
    public DMESimChamberRecipeMapBuilder copy() {
        return new DMESimChamberRecipeMapBuilder(this);
    }

    @SuppressWarnings("unused")
    public DMESimChamberRecipeMapBuilder dataItem(@NotNull ItemStack dataItem, int tier) {
        return dataItem(dataItem.getItem(), tier);
    }

    @SuppressWarnings("unused")
    public DMESimChamberRecipeMapBuilder dataItem(@NotNull Item dataItem, int tier) {
        setDataItem = true;
        var data = new DMEDataPropertyData(dataItem, tier);
        applyProperty(DMEDataProperty.getInstance(), data);

        var stack = new ItemStack(dataItem);
        DataModelHelper.setTierLevel(stack, tier);

        var input = new GTRecipeItemInput(stack);

        // If tier is minimum tier, allow empty tag OR equal tag
        if (MetadataManager.isMinDataModelTier(tier)) {
            input.setNBTMatchingCondition(EQUAL_TO_OR_NO_KEY,
                    NBTCondition.create(NBTTagType.INT, DataModelHelper.NBT_TIER, (long) tier));
        } else {
            input.setNBTMatchingCondition(NBTMatcher.EQUAL_TO,
                    NBTCondition.create(NBTTagType.INT, DataModelHelper.NBT_TIER, (long) tier));
        }

        notConsumable(input);
        return this;
    }

    @Override
    protected EnumValidationResult validate() {
        if (!setDataItem) {
            if (LabsGroovyHelper.isRunningGroovyScripts())
                GroovyLog.get().error("Error adding DME Sim Chamber Recipe: Data Item must not be null.");
            else
                NomiLabs.LOGGER.error("Error adding DME Sim Chamber Recipe: Data Item must not be null.");

            return EnumValidationResult.INVALID;
        }
        return super.validate();
    }

    @Override
    public DMESimChamberRecipeMapBuilder append(Recipe recipe, int multiplier, boolean multiplyDuration) {
        super.append(recipe, multiplier, multiplyDuration);
        var property = recipe.getProperty(DMEDataProperty.getInstance(), null);
        if (property != null) {
            setDataItem = true;
            property.setAddition(multiplier);
            applyProperty(DMEDataProperty.getInstance(), property);
        }
        return this;
    }

    // Returns true if tag is empty or doesn't have the key, otherwise performs NBTMatcher#EQUAL_TO.
    private static final NBTMatcher EQUAL_TO_OR_NO_KEY = (tag, condition) -> {
        // Bad Input
        if (condition == null || condition.tagType == null)
            return false;

        // Empty Tag: Allowed
        if (!NBTMatcher.hasKey(tag, condition.nbtKey, condition.tagType.typeId))
            return true;

        return NBTMatcher.EQUAL_TO.evaluate(tag, condition);
    };
}
