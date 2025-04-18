package com.nomiceu.nomilabs.mixin.ae2;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.Loader;

import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.config.LabsConfig;

import appeng.client.gui.AEBaseGui;
import appeng.client.gui.implementations.GuiInterfaceTerminal;
import appeng.client.gui.widgets.MEGuiTooltipTextField;

/**
 * Focuses the Interface Terminal on entry, so that keybinds (like Bogosort Cfg Keybind) doesn't register. Allows repeat
 * keyboard events. Fixes PMT Buttons.
 */
@Mixin(value = GuiInterfaceTerminal.class, remap = false)
public abstract class GuiInterfaceTerminalMixin extends AEBaseGui {

    @Shadow
    @Final
    private MEGuiTooltipTextField searchFieldNames;

    /**
     * Default Ignored Constructor
     */
    public GuiInterfaceTerminalMixin(Container container) {
        super(container);
    }

    @Inject(method = "initGui", at = @At("RETURN"), remap = true)
    private void focusGui(CallbackInfo ci) {
        Keyboard.enableRepeatEvents(true);
        searchFieldNames.setFocused(LabsConfig.modIntegration.ae2TerminalOptions.autoFocusInterface);

        // The original injection doesn't remap initGui, thus does not work.
        if (!Loader.isModLoaded(LabsValues.NAE2_MODID)) return;

        // Reflection because mixin
        try {
            // noinspection JavaReflectionMemberAccess
            Method initPmt = AEBaseGui.class.getDeclaredMethod("initializePatternMultiTool");
            initPmt.invoke(this);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            NomiLabs.LOGGER.fatal("[GuiInterfaceTerminalMixin] Failed to init PMT Buttons!");
        }
    }

    /**
     * The original injection doesn't remap drawScreen, thus does not work.
     */
    @Inject(method = "drawScreen",
            at = @At(value = "INVOKE", target = "Lappeng/client/gui/AEBaseGui;drawScreen(IIF)V"),
            require = 1,
            remap = true)
    private void properlyInjectAndAddPMTButtons(CallbackInfo ci) {
        if (!Loader.isModLoaded(LabsValues.NAE2_MODID)) return;

        // Reflection because mixin
        try {
            // noinspection JavaReflectionMemberAccess
            Field pmtButtons = AEBaseGui.class.getDeclaredField("patternMultiToolButtons");
            // noinspection unchecked
            List<? extends GuiButton> buttons = (List<? extends GuiButton>) pmtButtons.get(this);

            if (buttons != null)
                buttonList.addAll(buttons);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            NomiLabs.LOGGER.fatal("[GuiInterfaceTerminalMixin] Failed to add PMT Buttons!");
        }
    }

    @Unique
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
    }
}
