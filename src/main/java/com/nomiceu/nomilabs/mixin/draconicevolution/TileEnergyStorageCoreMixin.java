package com.nomiceu.nomilabs.mixin.draconicevolution;

import codechicken.lib.data.MCDataInput;
import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyStorageCore;
import com.nomiceu.nomilabs.integration.draconicevolution.DestructibleTileEnergyCore;
import com.nomiceu.nomilabs.integration.draconicevolution.TileEnergyStorageCoreLogic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TileEnergyStorageCore.class, remap = false)
public abstract class TileEnergyStorageCoreMixin implements DestructibleTileEnergyCore {
    @Shadow
    protected abstract long getCapacity();

    @Shadow
    protected abstract void updateStabilizers(boolean coreActive);

    @Shadow
    protected abstract void startBuilder(EntityPlayer client);

    @Inject(method = "activateCore", at = @At("HEAD"), cancellable = true)
    public void activateCore(CallbackInfo ci) {
        TileEnergyStorageCoreLogic.activateCore((TileEnergyStorageCore) (Object) this, getCapacity());
        updateStabilizers(true);
        ci.cancel();
    }

    @Inject(method = "deactivateCore", at = @At("HEAD"), cancellable = true)
    public void deactivateCore(CallbackInfo ci) {
        TileEnergyStorageCoreLogic.deactivateCore((TileEnergyStorageCore) (Object) this);
        updateStabilizers(false);
        ci.cancel();
    }

    @Inject(method = "validateStructure", at = @At("HEAD"), cancellable = true)
    public void validateStructure(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(TileEnergyStorageCoreLogic.validateStructure((TileEnergyStorageCore) (Object) this));
    }

    /**
     * Done here instead of in logic class because of private functions.
     */
    @Inject(method = "receivePacketFromClient", at = @At("HEAD"), cancellable = true)
    public void receivePacketFromClient(MCDataInput data, EntityPlayerMP client, int id, CallbackInfo ci) {
        var tile = (TileEnergyStorageCore) (Object) this;
        // Validate structure first (gui screen tile doesn't affect this one)
        tile.validateStructure();
        switch (id) {
            case 0:
                // Activate
                if (tile.active.value) {
                    tile.deactivateCore();
                }
                else {
                    tile.activateCore();
                }
                break;

            case 1:
                // Tier Up
                if (!tile.active.value && tile.tier.value < 8) {
                    tile.tier.value++;
                    tile.buildGuide.value = false;
                    tile.validateStructure();
                }
                break;

            case 2:
                // Tier Down
                if (!tile.active.value && tile.tier.value > 1) {
                    tile.tier.value--;
                    tile.buildGuide.value = false;
                    tile.validateStructure();
                }
                break;

            case 3:
                if (!tile.active.value && !tile.coreValid.value) {
                    tile.buildGuide.value = !tile.buildGuide.value;
                }
                break;

            case 4:
                if (!tile.active.value && !tile.coreValid.value) {
                    startBuilder(client);
                }
                break;

            case 7:
                if (tile.coreValid.value && !tile.active.value){
                    destructCore(client);
                }
                break;
        }
        ci.cancel();
    }

    @Unique
    @Override
    public void destructCore(EntityPlayer player) {
        TileEnergyStorageCoreLogic.destructCore((TileEnergyStorageCore) (Object) this, player);
    }
}
