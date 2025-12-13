package com.nomiceu.nomilabs.mixin.theoneprobe;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.network.NetworkTools;
import mcjty.theoneprobe.network.PacketGetInfo;

/**
 * On TheOneSmeagle; fixes get info not returning nbt with pickblock.
 * On TheOneProbe; Slightly reduces buffer sizes by using TOS's buffer write/read methods.
 */
@Mixin(value = PacketGetInfo.class, remap = false)
public class PacketGetInfoMixin {

    @Shadow
    private int dim;

    @Shadow
    private BlockPos pos;

    @Shadow
    private ProbeMode mode;

    @Shadow
    private EnumFacing sideHit;

    @Shadow
    private Vec3d hitVec;

    @Shadow
    private ItemStack pickBlock;

    @Inject(method = "fromBytes", at = @At("HEAD"), cancellable = true)
    private void newFromBytesLogic(ByteBuf buf, CallbackInfo ci) {
        dim = buf.readInt();
        pos = BlockPos.fromLong(buf.readLong());
        mode = ProbeMode.values()[buf.readByte()];

        byte sideByte = buf.readByte();
        sideHit = (sideByte == 127) ? null : EnumFacing.values()[sideByte];

        if (buf.readBoolean()) {
            float x = buf.readFloat();
            float y = buf.readFloat();
            float z = buf.readFloat();
            hitVec = new Vec3d(x, y, z);
        } else {
            hitVec = null;
        }

        pickBlock = NetworkTools.readItemStack(buf);
        ci.cancel();
    }

    @Inject(method = "toBytes", at = @At("HEAD"), cancellable = true)
    private void newToBytesLogic(ByteBuf buf, CallbackInfo ci) {
        buf.writeInt(dim);
        buf.writeLong(pos.toLong());
        buf.writeByte(mode.ordinal());
        buf.writeByte(sideHit == null ? 127 : sideHit.ordinal());

        if (hitVec == null) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            buf.writeFloat((float) hitVec.x);
            buf.writeFloat((float) hitVec.y);
            buf.writeFloat((float) hitVec.z);
        }

        NetworkTools.writeItemStack(buf, pickBlock);
        ci.cancel();
    }
}
