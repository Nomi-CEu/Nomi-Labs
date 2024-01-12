package com.nomiceu.nomilabs.mixin.draconicevolution;

import codechicken.lib.data.MCDataInput;
import com.brandon3055.brandonscore.blocks.TileBCBase;
import com.brandon3055.brandonscore.lib.Vec3I;
import com.brandon3055.brandonscore.lib.datamanager.ManagedBool;
import com.brandon3055.brandonscore.lib.datamanager.ManagedString;
import com.brandon3055.brandonscore.lib.datamanager.ManagedVec3I;
import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyStorageCore;
import com.brandon3055.draconicevolution.lib.EnergyCoreBuilder;
import com.nomiceu.nomilabs.integration.draconicevolution.EnergyCoreDestructor;
import com.nomiceu.nomilabs.integration.draconicevolution.ImprovedTileEnergyCore;
import com.nomiceu.nomilabs.integration.draconicevolution.StoppableProcess;
import com.nomiceu.nomilabs.integration.draconicevolution.TileEnergyStorageCoreLogic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
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

    @Unique
    private static final String EXPECTED_STRING = "expectedBlock";
    @Unique
    private static final String EXPECTED_POS = "expectedPos";

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

    @Unique
    public ManagedString expectedBlockString;

    @Unique
    public ManagedVec3I expectedBlockPos;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void initFields(CallbackInfo ci) {
        hasActiveBuilder = register(ACTIVE_BUILDER, new ManagedBool(false)).syncViaTile().saveToTile().trigerUpdate().finish();
        hasActiveDestructor = register(ACTIVE_DESTRUCTOR, new ManagedBool(false)).syncViaTile().saveToTile().trigerUpdate().finish();
        expectedBlockString = register(EXPECTED_STRING, new ManagedString("")).syncViaTile().saveToTile().trigerUpdate().finish();
        expectedBlockPos = register(EXPECTED_POS, new ManagedVec3I(new Vec3I(0, 0, 0))).syncViaTile().saveToTile().trigerUpdate().finish();
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
                if (!tile.active.value && !tile.coreValid.value && tile.tier.value != 1) {
                    tile.buildGuide.value = !tile.buildGuide.value;
                }
                break;

            case 4:
                if (!tile.active.value && !tile.coreValid.value && !hasActiveDestructor.value) {
                    startOrStopBuilder(client);
                }
                break;

            case 7:
                if (tile.coreValid.value && !tile.active.value && !hasActiveBuilder.value && tile.tier.value != 1){
                    startOrStopDestructor(client);
                }
                break;
        }
        ci.cancel();
    }

    /**
     * This allows the new features to actually be updated. This is split into two functions, one for Non Obfuscated Environments (Dev Env),
     * and one is for Obfuscated Environments (normal instances)
     * <p>
     * This is because {@link TileBCBase} has an {@link TileBCBase#update()} function,
     * but {@link TileEnergyStorageCore} also has an {@link TileEnergyStorageCore#update()} function.
     * <p>
     * Mixin fails to find the one in {@link TileEnergyStorageCore} if remap is true, but it can't find it in obfuscated environments
     * in runtime, if remap is false, because {@link TileEnergyStorageCore#update()} is overriding {@link ITickable}'s {@link ITickable#update()} function,
     * thus it is obfuscated.
     * <p>
     * The obfuscated name one does throw errors, but it works great in runtime, and doesn't crash in build time. This name will not change across forge versions,
     * or when loading with Cleanroom Loader.
     */
    @Inject(method = "update()V", at = @At("HEAD"))
    public void updateDevEnv(CallbackInfo ci) {
        updateLogic();
    }

    @SuppressWarnings({"UnresolvedMixinReference", "MixinAnnotationTarget"}) // Removes Errors/Warnings in IDE Inspections (not build time though)
    @Inject(method = "func_73660_a()V", at = @At("HEAD"))
    public void updateObf(CallbackInfo ci) {
        updateLogic();
    }

    /**
     * Shared Function so we don't have dupe code.
     */
    @Unique
    private void updateLogic() {
        var tile = (TileEnergyStorageCore) (Object) this;
        if (tile.getWorld().isRemote) return;
        if (activeBuilder == null) hasActiveBuilder.value = false; // Just in case
        else {
            if (activeBuilder.isDead())
                hasActiveBuilder.value = false;
            // Don't update process or set active builder to null, that is done in the original update function
        }

        if (activeDestructor == null) hasActiveDestructor.value = false; // Just in case
        else {
            if (activeDestructor.isDead()) {
                activeDestructor = null;
                hasActiveDestructor.value = false;
            } else
                activeDestructor.updateDestructProcess();
        }
    }

    @Unique
    private void startOrStopBuilder(EntityPlayer player) {
        if (hasActiveBuilder.value) {
            if (activeBuilder != null) // Just in case
                ((StoppableProcess) activeBuilder).stop();
            activeBuilder = null;
            hasActiveBuilder.value = false;
            return;
        }
        activeBuilder = new EnergyCoreBuilder((TileEnergyStorageCore) (Object) this, player);
        if (activeBuilder.isDead()) {
            hasActiveBuilder.value = false;
            activeBuilder = null;
        } else
            hasActiveBuilder.value = true;
    }

    @Unique
    private void startOrStopDestructor(EntityPlayer player) {
        if (hasActiveDestructor.value) {
            if (activeDestructor != null) // Just in case
                ((StoppableProcess) activeDestructor).stop();
            activeDestructor = null;
            hasActiveDestructor.value = false;
            return;
        }
        activeDestructor = new EnergyCoreDestructor((TileEnergyStorageCore) (Object) this, player);
        if (activeDestructor.isDead()) {
            hasActiveDestructor.value = false;
            activeDestructor = null;
        } else
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

    @Override
    @Unique
    public void onLoad() {
        super.onLoad();
        hasActiveBuilder.value = activeBuilder != null;
        hasActiveDestructor.value = activeDestructor != null;
    }

    @Override
    @Unique
    public void setExpectedBlockString(String string) {
        expectedBlockString.value = string;
    }

    @Override
    @Unique
    public void setExpectedBlockPos(BlockPos pos) {
        expectedBlockPos.vec = new Vec3I(pos);
    }

    @Override
    @Unique
    public String getExpectedBlockString() {
        return expectedBlockString.value;
    }

    @Override
    @Unique
    public Vec3I getExpectedBlockPos() {
        return expectedBlockPos.vec;
    }
}
