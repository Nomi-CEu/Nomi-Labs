package com.nomiceu.nomilabs.mixin.draconicevolution;

import codechicken.lib.data.MCDataInput;
import com.brandon3055.brandonscore.blocks.TileBCBase;
import com.brandon3055.brandonscore.lib.datamanager.ManagedBool;
import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyStorageCore;
import com.brandon3055.draconicevolution.lib.EnergyCoreBuilder;
import com.nomiceu.nomilabs.integration.draconicevolution.EnergyCoreDestructor;
import com.nomiceu.nomilabs.integration.draconicevolution.ImprovedTileEnergyCore;
import com.nomiceu.nomilabs.integration.draconicevolution.StoppableProcess;
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
public abstract class TileEnergyStorageCoreMixin extends TileBCBase implements ImprovedTileEnergyCore {

    @Unique
    private static final String ACTIVE_BUILDER = "activeBuilder";
    @Unique
    private static final String ACTIVE_DESTRUCTOR = "activeDestructor";

    @Shadow
    protected abstract long getCapacity();

    @Shadow
    protected abstract void updateStabilizers(boolean coreActive);

    @Shadow
    private EnergyCoreBuilder activeBuilder;

    @Unique
    private EnergyCoreDestructor activeDestructor;

    @Unique
    public ManagedBool hasActiveBuilder;

    @Unique
    public ManagedBool hasActiveDestructor;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void initFields(CallbackInfo ci) {
        hasActiveBuilder = register(ACTIVE_BUILDER, new ManagedBool(false)).syncViaTile().saveToTile().trigerUpdate().finish();
        hasActiveDestructor = register(ACTIVE_DESTRUCTOR, new ManagedBool(false)).syncViaTile().saveToTile().trigerUpdate().finish();
    }

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
                    startOrStopBuilder(client);
                }
                break;

            case 7:
                if (tile.coreValid.value && !tile.active.value){
                    startOrStopDestructor(client);
                }
                break;
        }
        ci.cancel();
    }

    @Inject(method = "update", at = @At("HEAD"))
    public void update(CallbackInfo ci) {
        var tile = (TileEnergyStorageCore) (Object) this;
        if (!tile.getWorld().isRemote) {
            if (activeDestructor != null) {
                if (activeDestructor.isDead()) {
                    activeDestructor = null;
                    hasActiveDestructor.value = false;
                } else
                    activeDestructor.updateDestructProcess();
            }
            if (activeBuilder != null) {
                if (activeBuilder.isDead())
                    hasActiveBuilder.value = false;
                // Don't update process or set active builder to null, thats done in the original update function
            }
        }
    }

    @Unique
    private void startOrStopBuilder(EntityPlayer player) {
        if (hasActiveBuilder.value) {
            ((StoppableProcess) activeBuilder).stop();
            activeBuilder = null;
            hasActiveBuilder.value = false;
            return;
        }
        activeBuilder = new EnergyCoreBuilder((TileEnergyStorageCore) (Object) this, player);
        hasActiveBuilder.value = true;
    }

    @Unique
    private void startOrStopDestructor(EntityPlayer player) {
        if (hasActiveDestructor.value) {
            ((StoppableProcess) activeDestructor).stop();
            activeDestructor = null;
            hasActiveDestructor.value = false;
            return;
        }
        activeDestructor = new EnergyCoreDestructor((TileEnergyStorageCore) (Object) this, player);
        hasActiveDestructor.value = true;
    }

    @Override
    @Unique
    public boolean hasActiveBuilder() {
        return hasActiveBuilder.value;
    }

    @Override
    @Unique
    public boolean hasActiveDestructor() {
        return hasActiveDestructor.value;
    }
}
