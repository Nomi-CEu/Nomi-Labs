package com.nomiceu.nomilabs.mixin.actuallyadditions;

import net.minecraft.nbt.NBTTagCompound;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.integration.actuallyadditions.AccessibleTileEntityPhantomface;

import de.ellpeck.actuallyadditions.mod.tile.TileEntityBase;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityPhantomface;

/**
 * Checks whether bound target is valid server-side, so that tile entities with custom capabilities
 * (e.g. NAE2's Storage Exposer) shows correctly in HUD.
 */
@Mixin(value = TileEntityPhantomface.class, remap = false)
public abstract class TileEntityPhantomfaceMixin implements AccessibleTileEntityPhantomface {

    @Shadow
    public abstract boolean isBoundThingInRange();

    @Unique
    private boolean labs$boundValid = false;

    @Unique
    @Override
    public boolean labs$boundValid() {
        return labs$boundValid;
    }

    @Inject(method = "writeSyncableNBT", at = @At("TAIL"))
    private void writeBoundValid(NBTTagCompound compound, TileEntityBase.NBTType type, CallbackInfo ci) {
        labs$boundValid = isBoundThingInRange();
        if (type != TileEntityBase.NBTType.SAVE_BLOCK)
            compound.setBoolean("labs$boundValid", labs$boundValid);
    }

    @Inject(method = "readSyncableNBT", at = @At("TAIL"))
    private void readBoundValid(NBTTagCompound compound, TileEntityBase.NBTType type, CallbackInfo ci) {
        if (type != TileEntityBase.NBTType.SAVE_BLOCK)
            labs$boundValid = compound.getBoolean("labs$boundValid");
    }
}
