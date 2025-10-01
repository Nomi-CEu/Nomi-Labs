package com.nomiceu.nomilabs.mixin.architecturecraft;

import net.minecraft.inventory.Container;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.elytradev.architecture.client.gui.GuiSawbench;
import com.elytradev.architecture.legacy.base.BaseGui;

/**
 * Changes the Gui Textures and Colors Used.
 */
@Mixin(value = GuiSawbench.class, remap = false)
public class GuiSawbenchMixin extends BaseGui.Screen {

    @Unique
    private static final String ORIGINAL_GUI_PATH = "gui/gui_sawbench.png";

    @Unique
    private static final String NEW_GUI_PATH = "gui/labs_gui_sawbench.png";

    @Unique
    private static final String ORIGINAL_GUI_BG_PATH = "gui/shapemenu_bg.png";

    @Unique
    private static final String NEW_GUI_BG_PATH = "gui/labs_shapemenu_bg.png";

    @Unique
    private static final String ORIGINAL_GUI_ITEMS_PATH = "gui/shapemenu_items.png";

    @Unique
    private static final String NEW_GUI_ITEMS_PATH = "gui/labs_shapemenu_items.png";

    /**
     * Default Ignored Constructor.
     */
    private GuiSawbenchMixin(Container container, int width, int height) {
        super(container, width, height);
    }

    @Redirect(method = "drawBackgroundLayer",
              at = @At(value = "INVOKE",
                       target = "Lcom/elytradev/architecture/client/gui/GuiSawbench;bindTexture(Ljava/lang/String;II)V"))
    private void bindNewGuiTexture(GuiSawbench instance, String texture, int u, int v) {
        if (texture.equals(ORIGINAL_GUI_PATH)) {
            instance.bindTexture(NEW_GUI_PATH, u, v);
            return;
        }
        instance.bindTexture(texture, u, v);
    }

    @Redirect(method = "drawPageMenu",
              at = @At(value = "INVOKE", target = "Lcom/elytradev/architecture/client/gui/GuiSawbench;setColor(DDD)V"))
    private void setHighlightColor(GuiSawbench instance, double r, double g, double b) {
        instance.setColor(0.0, 0.98, 0.94);
    }

    @Inject(method = "drawPageMenu",
            at = @At(value = "INVOKE",
                     target = "Lcom/elytradev/architecture/client/gui/GuiSawbench;gRestore()V",
                     shift = At.Shift.AFTER))
    private void setNewTextColor(CallbackInfo ci) {
        gSave();
        setTextColor(0, 0, 0);
    }

    @Inject(method = "drawPageMenu", at = @At(value = "TAIL"))
    private void restorePrevious(CallbackInfo ci) {
        gRestore();
    }

    @Redirect(method = "drawShapeMenu",
              at = @At(value = "INVOKE",
                       target = "Lcom/elytradev/architecture/client/gui/GuiSawbench;bindTexture(Ljava/lang/String;II)V"))
    private void bindNewGuiShapeTexture(GuiSawbench instance, String texture, int u, int v) {
        if (texture.equals(ORIGINAL_GUI_BG_PATH)) {
            instance.bindTexture(NEW_GUI_BG_PATH, u, v);
        }
        if (texture.equals(ORIGINAL_GUI_ITEMS_PATH)) {
            instance.bindTexture(NEW_GUI_ITEMS_PATH, u, v);
            return;
        }
        instance.bindTexture(texture, u, v);
    }
}
