package com.nomiceu.nomilabs.mixin.draconicevolution;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.brandon3055.draconicevolution.blocks.tileentity.TileInvisECoreBlock;
import com.nomiceu.nomilabs.integration.draconicevolution.TileInvisECoreBlockLogic;
import com.nomiceu.nomilabs.integration.draconicevolution.TileInvisECoreBlockState;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(value = TileInvisECoreBlock.class, remap = false)
public class TileInvisECoreBlockMixin implements TileInvisECoreBlockState {

    @Unique
    boolean labs$isDefault = true;
    @Unique
    int labs$metadata = 0;

    @Override
    public boolean labs$getDefault() {
        return labs$isDefault;
    }

    @Override
    public void labs$setIsDefault() {
        this.labs$isDefault = true;
        this.labs$metadata = 0;
    }

    public int labs$getMetadata() {
        return labs$metadata;
    }

    @Override
    public void labs$setMetadata(int metadata) {
        this.labs$metadata = metadata;
        this.labs$isDefault = false;
    }

    @Inject(method = "revert", at = @At("HEAD"), cancellable = true)
    public void revert(CallbackInfo ci) {
        TileInvisECoreBlockLogic.revert((TileInvisECoreBlock) (Object) this);
        ci.cancel();
    }

    @Inject(method = "getUpdatePacket()Lnet/minecraft/network/play/server/SPacketUpdateTileEntity;",
            at = @At("HEAD"),
            cancellable = true,
            remap = true)
    public void getUpdatePacket(CallbackInfoReturnable<SPacketUpdateTileEntity> cir) {
        cir.setReturnValue(TileInvisECoreBlockLogic.getUpdatePacket((TileInvisECoreBlock) (Object) this));
    }

    @Inject(method = "onDataPacket", at = @At("HEAD"), cancellable = true)
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt, CallbackInfo ci) {
        TileInvisECoreBlockLogic.onDataPacket((TileInvisECoreBlock) (Object) this, pkt);
        ci.cancel();
    }

    @Inject(method = "writeExtraNBT", at = @At("HEAD"), cancellable = true)
    public void writeExtraNBT(NBTTagCompound compound, CallbackInfo ci) {
        TileInvisECoreBlockLogic.writeExtraNBT((TileInvisECoreBlock) (Object) this, compound);
        ci.cancel();
    }

    @Inject(method = "readExtraNBT", at = @At("HEAD"), cancellable = true)
    public void readExtraNBT(NBTTagCompound compound, CallbackInfo ci) {
        TileInvisECoreBlockLogic.readExtraNBT((TileInvisECoreBlock) (Object) this, compound);
        ci.cancel();
    }
}
