package com.nomiceu.nomilabs.mixin.vanilla;

import java.util.List;
import java.util.Map;

import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.mixinhelper.AccessibleModListMessage;
import com.nomiceu.nomilabs.util.LabsModeHelper;

import io.netty.buffer.ByteBuf;

/**
 * Sends Labs Mode Data in Mod List Handshake.
 */
@Mixin(value = FMLHandshakeMessage.ModList.class, remap = false)
public class FMLHandshakeMessageModListMixin implements AccessibleModListMessage {

    @Shadow
    private Map<String, String> modTags;
    @Unique
    private static final String labs$modeId = "\0LABS_MODE_DATA\0";

    @Unique
    private String labs$mode = "";

    @Inject(method = "<init>(Ljava/util/List;)V", at = @At("TAIL"))
    private void initModeData(List<ModContainer> modList, CallbackInfo ci) {
        NomiLabs.LOGGER.info("[Labs Mode Validation] Injecting Mode Data into Mod List Handshake...");
        labs$mode = LabsModeHelper.getMode();

        modTags.put(labs$modeId, labs$mode);
    }

    @Inject(method = "fromBytes", at = @At("TAIL"))
    private void initModeFromBytes(ByteBuf buffer, CallbackInfo ci) {
        NomiLabs.LOGGER.info("[Labs Mode Validation] Reading Mode Data from Mod List Handshake...");
        labs$mode = modTags.get(labs$modeId);

        if (labs$mode == null) {
            NomiLabs.LOGGER.error("[Labs Mode Validation] No Mode Data in Mod List Handshake!");
            labs$mode = "";
        }
    }

    @Unique
    @Override
    public String labs$getMode() {
        return labs$mode;
    }
}
