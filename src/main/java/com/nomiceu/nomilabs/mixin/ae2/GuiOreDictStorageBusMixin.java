package com.nomiceu.nomilabs.mixin.ae2;

import java.io.IOException;

import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.NomiLabs;

import appeng.api.implementations.IUpgradeableHost;
import appeng.client.gui.implementations.GuiOreDictStorageBus;
import appeng.client.gui.implementations.GuiUpgradeable;
import appeng.client.gui.widgets.MEGuiTextField;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PacketValueConfig;
import appeng.util.item.OreDictFilterMatcher;

/**
 * Saves the ore dictionary entry on gui exit through escape, and autofocuses the text field. Allows repeat keyboard
 * events.
 */
@Mixin(value = GuiOreDictStorageBus.class, remap = false)
public class GuiOreDictStorageBusMixin extends GuiUpgradeable {

    @Shadow
    private MEGuiTextField searchFieldInputs;

    /**
     * Mandatory Ignored Constructor
     */
    private GuiOreDictStorageBusMixin(InventoryPlayer inventoryPlayer, IUpgradeableHost te) {
        super(inventoryPlayer, te);
    }

    @Inject(method = "addButtons", at = @At("RETURN"))
    private void autoFocus(CallbackInfo ci) {
        searchFieldInputs.setFocused(true);
    }

    @Inject(method = "keyTyped",
            at = @At(value = "INVOKE", target = "Lappeng/client/gui/widgets/MEGuiTextField;textboxKeyTyped(CI)Z"),
            require = 1,
            remap = true)
    private void checkEscape(char character, int key, CallbackInfo ci) {
        if (key == Keyboard.KEY_ESCAPE) {
            searchFieldInputs.setText(OreDictFilterMatcher.validateExp(searchFieldInputs.getText()));
            try {
                NetworkHandler.instance()
                        .sendToServer(new PacketValueConfig("OreDictStorageBus.save", searchFieldInputs.getText()));
            } catch (IOException e) {
                NomiLabs.LOGGER.fatal("[GuiOreDictStorageBusMixin] Failed to save Ore Regex on exit!");
            }
        }
    }

    @Unique
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        super.initGui();
    }

    @Unique
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
    }
}
