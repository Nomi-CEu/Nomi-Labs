package com.nomiceu.nomilabs.mixin.defaultworldgenerator;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ezrol.terry.minecraft.defaultworldgenerator.gui.DefaultWorldSelectionList;
import com.nomiceu.nomilabs.util.LabsTranslate;

/**
 * Override the adding of buttons.
 */
@Mixin(value = DefaultWorldSelectionList.class, remap = false)
public abstract class DefaultWorldSelectionListMixin extends GuiScreen {

    @Shadow
    private GuiScreen parent;

    @Shadow
    @Final
    private static int BTN_OK;

    @Unique
    private static final int BTN_CANCEL = 401;

    @Redirect(method = "initGui",
              at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"),
              remap = true)
    public boolean initNewGui(List<GuiButton> instance, Object e) {
        instance.add(new GuiButton(BTN_OK,
                width / 2 - 155, height - 28, 150, 20,
                LabsTranslate.translate("defaultworldgenerator-port.chooseworld.gui.continue")));
        return instance.add(
                new GuiButton(BTN_CANCEL, width / 2 + 5, height - 28, 150, 20, LabsTranslate.translate("gui.cancel")));
    }

    @Inject(method = "actionPerformed", at = @At(value = "HEAD"), remap = true)
    public void handleCancelAction(GuiButton button, CallbackInfo ci) {
        if (button.id == BTN_CANCEL) {
            mc.displayGuiScreen(parent);
        }
    }
}
