package com.nomiceu.nomilabs.groovy;

import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.nomiceu.nomilabs.util.ItemMeta;
import com.nomiceu.nomilabs.util.LabsTranslate;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class NBTClearingRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    // Map of Output to Warning Tooltip + Input
    public static final Map<ItemMeta, Map<ItemMeta, LabsTranslate.Translatable>> NBT_CLEARERS = new Object2ObjectOpenHashMap<>();
    public static final LabsTranslate.Translatable WARNING_TOOLTIP = new LabsTranslate.Translatable(
            "tooltip.nomilabs.recipe.clearing");
    public static final LabsTranslate.Translatable CAN_CLEAR_TOOLTIP = new LabsTranslate.Translatable(
            "tooltip.nomilabs.item.can_clear");

    @Nullable
    private final Consumer<ItemStack> nbtClearer;
    private final ItemStack singleInput;
    private final ItemStack exampleOutput;

    public NBTClearingRecipe(ItemStack input, ItemStack exampleOutput, @Nullable Consumer<ItemStack> nbtClearer) {
        this.singleInput = input;
        this.exampleOutput = exampleOutput;
        this.nbtClearer = nbtClearer;
    }

    @Override
    public boolean matches(@NotNull InventoryCrafting inv, @NotNull World worldIn) {
        boolean found = false;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            var stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            if (found || !ItemMeta.compare(inv.getStackInSlot(i), singleInput)) return false;
            found = true;
        }
        return found;
    }

    @Override
    @NotNull
    public ItemStack getCraftingResult(@NotNull InventoryCrafting inv) {
        if (nbtClearer == null) return exampleOutput.copy();

        var stack = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) break;
        }

        if (stack.isEmpty()) return ItemStack.EMPTY;
        var display = new ItemStack(exampleOutput.getItem(), 1, exampleOutput.getMetadata());
        display.setTagCompound(stack.getTagCompound());
        nbtClearer.accept(display);
        return display;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    @NotNull
    public ItemStack getRecipeOutput() {
        return exampleOutput;
    }

    /**
     * Makes Buckets be Consumed
     */
    @Override
    @NotNull
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
    }
}
