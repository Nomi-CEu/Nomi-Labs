package com.nomiceu.nomilabs.mixin.architecturecraft;

import com.elytradev.architecture.client.gui.GuiSawbench;
import com.elytradev.architecture.legacy.base.BaseGui;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Changes the Gui Shape Item Texture Used.
 */
@Mixin(value = GuiSawbench.class, remap = false)
public class GuiSawbenchMixin extends BaseGui.Screen {
    @Unique
    private static final String ORIGINAL_GUI_ITEMS_PATH = "gui/shapemenu_items.png";

    @Unique
    private static final String NEW_GUI_ITEMS_PATH = "gui/labs_shapemenu_items.png";

    /**
     * Default Ignored Constructor.
     */
    public GuiSawbenchMixin(Container container, int width, int height) {
        super(container, width, height);
    }

    @Redirect(method = "drawShapeMenu", at = @At(value = "INVOKE", target = "Lcom/elytradev/architecture/client/gui/GuiSawbench;bindTexture(Ljava/lang/String;II)V"), remap = false)
    public void bindNewGuiTexture(GuiSawbench instance, String texture, int u, int v) {
        if (texture.equals(ORIGINAL_GUI_ITEMS_PATH)) {
            instance.bindTexture(NEW_GUI_ITEMS_PATH, u, v);
            return;
        }
        instance.bindTexture(texture, u, v);
    }
}
