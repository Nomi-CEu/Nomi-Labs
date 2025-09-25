package com.nomiceu.nomilabs.mixin.draconicevolution;

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

import codechicken.lib.data.MCDataInput;

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
    private EnergyCoreDestructor labs$activeDestructor;

    @Unique
    public ManagedBool labs$hasActiveBuilder;

    @Unique
    public ManagedBool labs$hasActiveDestructor;

    @Unique
    public ManagedString labs$expectedBlockString;

    @Unique
    public ManagedVec3I labs$expectedBlockPos;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initFields(CallbackInfo ci) {
        labs$hasActiveBuilder = register(ACTIVE_BUILDER, new ManagedBool(false)).syncViaTile().saveToTile()
                .trigerUpdate()
                .finish();
        labs$hasActiveDestructor = register(ACTIVE_DESTRUCTOR, new ManagedBool(false)).syncViaTile().saveToTile()
                .trigerUpdate().finish();
        labs$expectedBlockString = register(EXPECTED_STRING, new ManagedString("")).syncViaTile().saveToTile()
                .trigerUpdate()
                .finish();
        labs$expectedBlockPos = register(EXPECTED_POS, new ManagedVec3I(new Vec3I(0, 0, 0))).syncViaTile().saveToTile()
                .trigerUpdate().finish();
    }

    @Inject(method = "activateCore", at = @At("HEAD"), cancellable = true)
    private void activateCore(CallbackInfo ci) {
        TileEnergyStorageCoreLogic.activateCore((TileEnergyStorageCore) (Object) this, getCapacity());
        updateStabilizers(true);
        ci.cancel();
    }

    @Inject(method = "deactivateCore", at = @At("HEAD"), cancellable = true)
    private void deactivateCore(CallbackInfo ci) {
        TileEnergyStorageCoreLogic.deactivateCore((TileEnergyStorageCore) (Object) this);
        updateStabilizers(false);
        ci.cancel();
    }

    @Inject(method = "validateStructure", at = @At("HEAD"), cancellable = true)
    private void validateStructure(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(TileEnergyStorageCoreLogic.validateStructure((TileEnergyStorageCore) (Object) this));
    }

    /**
     * Done here instead of in logic class because of private functions.
     */
    @Inject(method = "receivePacketFromClient", at = @At("HEAD"), cancellable = true)
    private void receivePacketFromClient(MCDataInput data, EntityPlayerMP client, int id, CallbackInfo ci) {
        var tile = (TileEnergyStorageCore) (Object) this;
        // Validate structure first (gui screen tile doesn't affect this one)
        tile.validateStructure();
        switch (id) {
            case 0:
                // Activate
                if (tile.active.value) {
                    tile.deactivateCore();
                } else {
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
                if (!tile.active.value && !tile.coreValid.value && !labs$hasActiveDestructor.value) {
                    labs$startOrStopBuilder(client);
                }
                break;

            case 7:
                if (tile.coreValid.value && !tile.active.value && !labs$hasActiveBuilder.value &&
                        tile.tier.value != 1) {
                    labs$startOrStopDestructor(client);
                }
                break;
        }
        ci.cancel();
    }

    /**
     * This allows the new features to actually be updated. This is split into two functions, one for Non Obfuscated
     * Environments (Dev Env),
     * and one is for Obfuscated Environments (normal instances)
     * <p>
     * This is because {@link TileBCBase} has an {@link TileBCBase#update()} function,
     * but {@link TileEnergyStorageCore} also has an {@link TileEnergyStorageCore#update()} function.
     * <p>
     * Mixin fails to find the one in {@link TileEnergyStorageCore} if remap is true, but it can't find it in obfuscated
     * environments
     * in runtime, if remap is false, because {@link TileEnergyStorageCore#update()} is overriding {@link ITickable}'s
     * {@link ITickable#update()} function,
     * thus it is obfuscated.
     * <p>
     * The obfuscated name one does throw errors, but it works great in runtime, and doesn't crash in build time. This
     * name will not change across forge versions,
     * or when loading with Cleanroom Loader.
     */
    @Inject(method = "update()V", at = @At("HEAD"))
    private void updateDevEnv(CallbackInfo ci) {
        labs$updateLogic();
    }

    @SuppressWarnings({ "UnresolvedMixinReference", "MixinAnnotationTarget" })
    @Inject(method = "func_73660_a()V", at = @At("HEAD"))
    private void updateObf(CallbackInfo ci) {
        labs$updateLogic();
    }

    /**
     * Shared Function so we don't have dupe code.
     */
    @Unique
    private void labs$updateLogic() {
        var tile = (TileEnergyStorageCore) (Object) this;
        if (tile.getWorld().isRemote) return;
        if (activeBuilder == null) labs$hasActiveBuilder.value = false; // Just in case
        else {
            if (activeBuilder.isDead())
                labs$hasActiveBuilder.value = false;
            // Don't update process or set active builder to null, that is done in the original update function
        }

        if (labs$activeDestructor == null) labs$hasActiveDestructor.value = false; // Just in case
        else {
            if (labs$activeDestructor.isDead()) {
                labs$activeDestructor = null;
                labs$hasActiveDestructor.value = false;
            } else
                labs$activeDestructor.updateDestructProcess();
        }
    }

    @Unique
    private void labs$startOrStopBuilder(EntityPlayer player) {
        if (labs$hasActiveBuilder.value) {
            if (activeBuilder != null) // Just in case
                ((StoppableProcess) activeBuilder).labs$stop();
            activeBuilder = null;
            labs$hasActiveBuilder.value = false;
            return;
        }
        activeBuilder = new EnergyCoreBuilder((TileEnergyStorageCore) (Object) this, player);
        if (activeBuilder.isDead()) {
            labs$hasActiveBuilder.value = false;
            activeBuilder = null;
        } else
            labs$hasActiveBuilder.value = true;
    }

    @Unique
    private void labs$startOrStopDestructor(EntityPlayer player) {
        if (labs$hasActiveDestructor.value) {
            if (labs$activeDestructor != null) // Just in case
                ((StoppableProcess) labs$activeDestructor).labs$stop();
            labs$activeDestructor = null;
            labs$hasActiveDestructor.value = false;
            return;
        }
        labs$activeDestructor = new EnergyCoreDestructor((TileEnergyStorageCore) (Object) this, player);
        if (labs$activeDestructor.isDead()) {
            labs$hasActiveDestructor.value = false;
            labs$activeDestructor = null;
        } else
            labs$hasActiveDestructor.value = true;
    }

    @Unique
    @Override
    public boolean labs$hasActiveBuilder() {
        return labs$hasActiveBuilder.value;
    }

    @Unique
    @Override
    public boolean labs$hasActiveDestructor() {
        return labs$hasActiveDestructor.value;
    }

    @Unique
    @Override
    public void onLoad() {
        super.onLoad();
        labs$hasActiveBuilder.value = activeBuilder != null;
        labs$hasActiveDestructor.value = labs$activeDestructor != null;
    }

    @Unique
    @Override
    public void labs$setExpectedBlockString(String string) {
        labs$expectedBlockString.value = string;
    }

    @Unique
    @Override
    public void labs$setExpectedBlockPos(BlockPos pos) {
        labs$expectedBlockPos.vec = new Vec3I(pos);
    }

    @Unique
    @Override
    public String labs$getExpectedBlockString() {
        return labs$expectedBlockString.value;
    }

    @Unique
    @Override
    public Vec3I labs$getExpectedBlockPos() {
        return labs$expectedBlockPos.vec;
    }
}
