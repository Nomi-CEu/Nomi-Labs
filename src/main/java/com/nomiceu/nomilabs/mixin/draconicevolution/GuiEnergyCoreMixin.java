package com.nomiceu.nomilabs.mixin.draconicevolution;

import com.brandon3055.draconicevolution.client.gui.GuiEnergyCore;
import com.nomiceu.nomilabs.integration.draconicevolution.DraconicHelpers;
import com.nomiceu.nomilabs.integration.draconicevolution.GuiEnergyCoreLogic;
import com.nomiceu.nomilabs.integration.draconicevolution.ImprovedTileEnergyCore;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// TODO maybe one day improve the GUI, so you can destruct button at any time, and it isn't as crowded with buttons disappearing
// Perhaps inspire by GT's?
/**
 * Adds destruct core to the list of buttons. Some methods here are directly implemented instead of calling logic, which
 * may be changed in the future, but is just easier because of all the variable setting and protected methods.
 */
@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(value = GuiEnergyCore.class, remap = false)
public abstract class GuiEnergyCoreMixin extends GuiContainer {
    @Shadow
    private GuiButton toggleGuide;

    @Shadow
    private GuiButton tierUp;

    @Shadow
    private GuiButton tierDown;

    @Shadow
    private GuiButton assembleCore;

    @Shadow
    private GuiButton activate;

    @Unique
    private GuiButton destructCore;

    /**
     * Ignored mandatory constructor, so we can use protected fields
     */
    @SuppressWarnings("unused")
    public GuiEnergyCoreMixin(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @Inject(method = "initGui()V", at = @At(value = "INVOKE", target = "Lcom/brandon3055/draconicevolution/client/gui/GuiEnergyCore;updateButtonStates()V", remap = false), remap = true, require = 1)
    public void initGui(CallbackInfo ci) {
        destructCore = GuiEnergyCoreLogic.addDestructButtonToList(this, buttonList);
        // Change default display string of assemble core
        assembleCore.displayString = I18n.format("button.de.assembleCore.instant.txt");
    }

    @Inject(method = "drawGuiContainerBackgroundLayer(FII)V", at = @At("HEAD"), cancellable = true, remap = true)
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY, CallbackInfo ci) {
        var guiCore = (GuiEnergyCore) (Object) this;
        // We only need to change the background layer of non-active cores (changing wrap text dimensions for non-valid)
        if (guiCore.tile.active.value)
            return;
        GuiEnergyCoreLogic.drawBackground(guiCore, fontRenderer);
        ci.cancel();
    }

    @Inject(method = "updateButtonStates", at = @At("TAIL"))
    private void updateButtonStates(CallbackInfo ci) {
        var guiCore = (GuiEnergyCore) (Object) this;
        var improvedTile = (ImprovedTileEnergyCore) guiCore.tile;
        tierUp.visible = tierDown.visible = !guiCore.tile.active.value;
        // Have toggle guide be visible but disabled when tier is 1
        toggleGuide.visible =  ((!guiCore.tile.coreValid.value && !improvedTile.hasActiveDestructor()) || guiCore.tile.tier.value == 1) && !guiCore.tile.active.value;
        destructCore.visible = (guiCore.tile.coreValid.value || improvedTile.hasActiveDestructor()) && !guiCore.tile.active.value && guiCore.tile.tier.value != 1;
        toggleGuide.enabled = guiCore.tile.tier.value != 1;

        activate.enabled = guiCore.tile.coreValid.value && guiCore.tile.stabilizersOK.value;

        assembleCore.enabled = !improvedTile.hasActiveDestructor();
        if (DraconicHelpers.instantBuilder())
            assembleCore.displayString = I18n.format("button.de.assembleCore.instant.txt");
        else {
            if (improvedTile.hasActiveBuilder())
                assembleCore.displayString = I18n.format("button.de.assembleCore.stop.txt");
            else
                assembleCore.displayString = I18n.format("button.de.assembleCore.start.txt");
        }

        destructCore.enabled = !improvedTile.hasActiveBuilder();
        if (DraconicHelpers.instantDestructor())
            destructCore.displayString = I18n.format("button.de.destructCore.instant.txt");
        else {
            if (improvedTile.hasActiveDestructor())
                destructCore.displayString = I18n.format("button.de.destructCore.stop.txt");
            else
                destructCore.displayString = I18n.format("button.de.destructCore.start.txt");
        }
    }

    @Inject(method = "updateScreen()V", at = @At("HEAD"), remap = true)
    public void updateScreen(CallbackInfo ci) {
        var guiCore = (GuiEnergyCore) (Object) this;
        // Reload whether the structure is valid (Fixes needing to exit GUI before able to activate structure after auto-building/destructing)
        guiCore.tile.validateStructure();
    }

    @Inject(method = "actionPerformed(Lnet/minecraft/client/gui/GuiButton;)V", at = @At("TAIL"), cancellable = true, remap = true)
    public void actionPerformed(GuiButton button, CallbackInfo ci) {
        if (!(button.id == destructCore.id))
            return;

        var guiCore = (GuiEnergyCore) (Object) this;
        guiCore.tile.sendPacketToServer(output -> {
        }, button.id);

        ci.cancel();
    }
}
