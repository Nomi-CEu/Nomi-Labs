package com.nomiceu.nomilabs.mixin.nuclearcraft;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import nc.config.NCConfig;
import nc.network.tile.FissionUpdatePacket;
import nc.recipe.ProcessorRecipeHandler;
import nc.tile.IGui;
import nc.tile.generator.TileFissionController;
import nc.tile.generator.TileItemGenerator;
import nc.tile.internal.inventory.ItemSorption;

@Mixin(value = TileFissionController.class, remap = false)
public abstract class TileFissionControllerMixin extends TileItemGenerator implements IGui<FissionUpdatePacket> {

    @Shadow
    public double heat;

    @Shadow
    public abstract int getMaxHeat();

    @Shadow
    public double heatChange;

    @Shadow
    public abstract boolean isProcessing();

    @Shadow
    public double cooling;

    @Shadow
    public abstract int getComparatorStrength();

    @Shadow
    public abstract boolean findAdjacentComparator();

    @Shadow
    public int comparatorStrength;

    @Unique
    private boolean labs$prevProcessing = false;

    /**
     * Mandatory Ignored Constructor
     */
    private TileFissionControllerMixin(String name, int itemInSize, int itemOutSize, int otherSize,
                                       @NotNull List<ItemSorption> itemSorptions, int capacity,
                                       @NotNull ProcessorRecipeHandler recipeHandler) {
        super(name, itemInSize, itemOutSize, otherSize, itemSorptions, capacity, recipeHandler);
    }

    @Inject(method = "updateGenerator", at = @At("HEAD"))
    private void saveProcessing(CallbackInfo ci) {
        labs$prevProcessing = isProcessing();
    }

    @Inject(method = "overheat", at = @At("RETURN"), cancellable = true)
    private void properOverheatReturn(CallbackInfoReturnable<Boolean> cir) {
        if (heat < getMaxHeat() || NCConfig.fission_overheat)
            return;

        cir.setReturnValue(true);
        getRadiationSource().setRadiationLevel(0.0);

        boolean processing = isProcessing();

        // Remove Heat from Fuels, then Add Cooling Heat
        heat = Math.max(0, heat - heatChange + cooling);

        /* Update Tile If Needed */
        boolean shouldUpdate = false;
        if (labs$prevProcessing != processing) {
            shouldUpdate = true;
            this.updateBlockType();
            this.sendUpdateToAllPlayers();
        }

        int compStrength = getComparatorStrength();
        if (comparatorStrength != compStrength && findAdjacentComparator()) {
            shouldUpdate = true;
        }

        this.comparatorStrength = compStrength;
        this.sendUpdateToListeningPlayers();
        if (shouldUpdate) {
            this.markDirty();
        }
    }
}
