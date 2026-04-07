package com.nomiceu.nomilabs.mixin.ae2;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.inventory.Container;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.network.LabsFluidConfigTermJEIMessage;
import com.nomiceu.nomilabs.network.LabsNetworkHandler;

import appeng.client.gui.AEBaseGui;
import appeng.client.gui.implementations.GuiFluidInterfaceConfigurationTerminal;
import appeng.client.gui.widgets.GuiCustomSlot;
import appeng.client.gui.widgets.MEGuiTextField;
import appeng.client.me.ClientDCInternalFluidInv;
import appeng.fluids.client.gui.widgets.GuiFluidTank;
import mezz.jei.api.gui.IGhostIngredientHandler;

/**
 * Allows Auto-Focusing of the Fluid Interface Configuration Terminal. Allows Not Saving Searches. Allows Repeat
 * Keyboard Events. Fixes JEI Ghost Fluid Dragging.
 */
@Mixin(value = GuiFluidInterfaceConfigurationTerminal.class, remap = false)
public abstract class GuiFluidInterfaceConfigurationTerminalMixin extends AEBaseGui {

    @Shadow
    private MEGuiTextField searchFieldInputs;

    @Shadow
    @Final
    private Map<GuiFluidTank, ClientDCInternalFluidInv> guiFluidTankClientDCInternalFluidInvMap;

    /**
     * Default Ignored Constructor
     */
    private GuiFluidInterfaceConfigurationTerminalMixin(Container container) {
        super(container);
    }

    @Inject(method = "initGui", at = @At("RETURN"), remap = true)
    private void focusGui(CallbackInfo ci) {
        searchFieldInputs.setFocused(LabsConfig.modIntegration.ae2TerminalOptions.autoFocusConfigFluidInterface);
        if (!LabsConfig.modIntegration.ae2TerminalOptions.saveConfigInterfaceSearch)
            searchFieldInputs.setText("");
        Keyboard.enableRepeatEvents(true);
    }

    @Inject(method = "onGuiClosed", at = @At("HEAD"))
    private void disableRepeatEvents(CallbackInfo ci) {
        Keyboard.enableRepeatEvents(false);
    }

    // Completely replace; just too complex of a change
    @Inject(method = "getPhantomTargets", at = @At("HEAD"), cancellable = true)
    private void newPhantomTargetsLogic(Object ingredient,
                                        CallbackInfoReturnable<List<IGhostIngredientHandler.Target<?>>> cir) {
        if (!(ingredient instanceof FluidStack fluid)) {
            cir.setReturnValue(Collections.emptyList());
            return;
        }

        List<IGhostIngredientHandler.Target<?>> targets = new ArrayList<>();
        for (GuiCustomSlot guiSlot : guiSlots) {
            if (!(guiSlot instanceof GuiFluidTank tank)) continue;

            IGhostIngredientHandler.Target<Object> target = new IGhostIngredientHandler.Target<>() {

                @Override
                @NotNull
                public Rectangle getArea() {
                    return new Rectangle(getGuiLeft() + tank.xPos(), getGuiTop() + tank.yPos(), 16, 16);
                }

                @Override
                public void accept(@NotNull Object ingredient) {
                    LabsNetworkHandler.NETWORK_HANDLER.sendToServer(
                            new LabsFluidConfigTermJEIMessage(tank.getId(),
                                    guiFluidTankClientDCInternalFluidInvMap.get(tank).getId(), fluid));
                }
            };
            targets.add(target);
        }

        cir.setReturnValue(targets);
    }
}
