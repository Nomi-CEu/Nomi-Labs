package com.nomiceu.nomilabs.groovy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Optional;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.integration.nuclearcraft.AccessibleCoolerType;

import nc.config.NCConfig;
import nc.enumm.MetaEnums;
import nc.recipe.NCRecipes;
import nc.recipe.other.ActiveCoolerRecipes;

public class NCActiveCoolerHelper {

    /*
     * Reference must not be CHANGED! References are stored in TileActiveCooler!
     * ID - 1 -> Fluid Name
     */
    public static final List<String> fluidNamesFromIDs = new ArrayList<>();

    static {
        reloadFluidNames();
    }

    @SuppressWarnings("ConstantValue")
    private static void reloadFluidNames() {
        fluidNamesFromIDs.clear();
        for (int i = 1; i < MetaEnums.CoolerType.values().length; i++) {
            fluidNamesFromIDs.add(i - 1,
                    ((AccessibleCoolerType) (Object) MetaEnums.CoolerType.values()[i]).getOriginalFluidName());
        }
    }

    public static void onReload() {
        // Usually Active Cooler is null on Pre-Init or Init Load
        if (NCRecipes.active_cooler == null) return;

        reloadFluidNames();

        NCRecipes.active_cooler.removeAllRecipes();
        NCRecipes.active_cooler.addRecipes();
    }

    public static void afterScriptLoad() {
        // Usually Active Cooler is null on Pre-Init or Init Load
        if (NCRecipes.active_cooler == null) return;

        NCRecipes.active_cooler.refreshCache();
    }

    @Optional.Method(modid = LabsValues.NUCLEARCRAFT_MODID)
    public static void changeCoolerRecipe(FluidStack newStack, MetaEnums.CoolerType type) {
        if (NCRecipes.active_cooler == null) {
            GroovyLog.get().error("Active Cooler Recipes not Found!");
            return;
        }

        boolean removed = false;
        for (var recipe : NCRecipes.active_cooler.getRecipeList()) {
            if (recipe.fluidIngredients().size() == 1) {
                var ing = recipe.fluidIngredients().get(0);
                if (ing.getInputStackList().size() == 1 &&
                        Objects.equals(ing.getInputStackList().get(0).getFluid().getName(), type.getFluidName())) {
                    NCRecipes.active_cooler.removeRecipe(recipe);
                    removed = true;
                    break;
                }
            }
        }
        if (!removed) {
            GroovyLog.get().error("Could not find ActiveCooler recipe for " + type.getFluidName() + "!");
        }

        NCRecipes.active_cooler.addRecipe(
                ActiveCoolerRecipes.fluidStack(newStack.getFluid().getName(), NCConfig.active_cooler_max_rate),
                Math.round(NCConfig.fission_active_cooling_rate[type.getID() - 1] * NCConfig.active_cooler_max_rate /
                        20.0),
                Math.round(NCConfig.fusion_active_cooling_rate[type.getID() - 1] * NCConfig.active_cooler_max_rate /
                        20.0));
        fluidNamesFromIDs.set(type.getID() - 1, newStack.getFluid().getName());
    }
}
