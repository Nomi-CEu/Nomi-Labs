package com.nomiceu.nomilabs.mixin.ae2;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.integration.ae2.InclNonConsumeSettable;
import com.nomiceu.nomilabs.integration.ae2.LabsInclNonConsumableButton;

import appeng.api.storage.ITerminalHost;
import appeng.client.gui.implementations.GuiMEMonitorable;
import appeng.client.gui.implementations.GuiPatternTerm;
import appeng.container.implementations.ContainerPatternEncoder;
import appeng.core.AELog;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PacketValueConfig;

/**
 * Adds the labs non-consume button to AE2.
 */
@Mixin(value = GuiPatternTerm.class, remap = false)
public class GuiPatternTermMixin extends GuiMEMonitorable {

    @Shadow
    @Final
    private ContainerPatternEncoder container;

    @Unique
    private LabsInclNonConsumableButton labs$inclNonConsume;

    /**
     * Mandatory Ignored Constructor
     */
    private GuiPatternTermMixin(InventoryPlayer inventoryPlayer, ITerminalHost te) {
        super(inventoryPlayer, te);
    }

    @Inject(method = "initGui", at = @At("RETURN"), remap = true)
    private void initCustomButton(CallbackInfo ci) {
        labs$inclNonConsume = new LabsInclNonConsumableButton(guiLeft + 84, guiTop + ySize - 163);
        buttonList.add(labs$inclNonConsume);
    }

    @Inject(method = "actionPerformed",
            at = @At(value = "INVOKE",
                     target = "Lappeng/client/gui/implementations/GuiMEMonitorable;actionPerformed(Lnet/minecraft/client/gui/GuiButton;)V",
                     shift = At.Shift.AFTER),
            require = 1,
            remap = true)
    private void checkCustomButton(GuiButton btn, CallbackInfo ci) {
        if (labs$inclNonConsume == btn) {
            labs$inclNonConsume.toggle();

            // Client Sync
            ((InclNonConsumeSettable) container)
                    .labs$setInclNonConsume(labs$inclNonConsume.isInclNonConsume());

            // Server Sync
            try {
                NetworkHandler.instance().sendToServer(
                        new PacketValueConfig("Labs$NonConsume", labs$inclNonConsume.isInclNonConsume() ? "1" : "0"));
            } catch (IOException e) {
                AELog.error(e);
            }
        }
    }

    @Inject(method = "drawFG", at = @At("HEAD"))
    private void syncNonConsume(int offsetX, int offsetY, int mouseX, int mouseY, CallbackInfo ci) {
        if (container.isCraftingMode())
            labs$inclNonConsume.visible = false;
        else {
            labs$inclNonConsume.visible = true;
            labs$inclNonConsume.setInclNonConsume(((InclNonConsumeSettable) container).labs$inclNonConsume());
        }
    }
}
