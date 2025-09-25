package com.nomiceu.nomilabs.mixin.betterp2p;

import static com.nomiceu.nomilabs.integration.betterp2p.ModeDescriptionsHandler.labsModeDescriptions;
import static com.projecturanus.betterp2p.client.gui.GuiAdvancedMemoryCardKt.*;
import static com.projecturanus.betterp2p.client.gui.GuiHelperKt.drawTexturedQuad;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nomiceu.nomilabs.LabsTextures;
import com.nomiceu.nomilabs.integration.betterp2p.AccessibleGuiAdvancedMemoryCard;
import com.nomiceu.nomilabs.integration.betterp2p.LabsBetterMemoryCardModes;
import com.projecturanus.betterp2p.client.gui.GuiAdvancedMemoryCard;
import com.projecturanus.betterp2p.client.gui.widget.WidgetButton;
import com.projecturanus.betterp2p.item.BetterMemoryCardModes;

/**
 * Fixes ArrayOutOfBoundsException, Tooltips and Icons with New Modes.
 */
@Mixin(targets = "com.projecturanus.betterp2p.client.gui.GuiAdvancedMemoryCard$2", remap = false)
public abstract class ModeWidgetButtonMixin extends WidgetButton {

    @Unique
    private static final double labs$textureSize = 32.0;

    @Unique
    protected double labs$texX;

    @Unique
    protected double labs$texY;

    @Unique
    protected int labs$iconId;

    @Unique
    protected boolean labs$useLabsTexture;

    /**
     * Mandatory Ignored Constructor
     */
    private ModeWidgetButtonMixin(@NotNull GuiAdvancedMemoryCard gui, int x, int y, int width, int height) {
        super(gui, x, y, width, height);
    }

    @Redirect(method = "<init>",
              at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"),
              require = 1)
    private Object labs$getDescription(List<List<String>> instance, int index) {
        var mode = BetterMemoryCardModes.values()[index];
        return labsModeDescriptions.get(mode);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void setValues(GuiAdvancedMemoryCard $outer, GuiAdvancedMemoryCard gui, int x, int y, int width, int height,
                           CallbackInfo ci) {
        labs$setTextureDetails();
    }

    @Unique
    @Override
    public void draw(@NotNull Minecraft mc, int mouseX, int mouseY, float partial) {
        Tessellator tessellator = Tessellator.getInstance();
        super.drawBG(tessellator, mouseX, mouseY, partial);

        // If New Labs Mode, Use New Texture
        if (labs$useLabsTexture)
            LabsTextures.P2P_CUSTOM_MODES[labs$iconId].draw(x + 1.0, y + 1.0, width - 2, height - 2);
        else {
            drawTexturedQuad(
                    tessellator,
                    x + 1.0,
                    y + 1.0,
                    x + width - 1.0,
                    y + height - 1.0,
                    labs$texX / GUI_WIDTH,
                    labs$texY / GUI_TEX_HEIGHT,
                    (labs$texX + width) / GUI_WIDTH,
                    (labs$texY + height) / GUI_TEX_HEIGHT);
        }
    }

    @Inject(method = "mousePressed", at = @At("HEAD"), cancellable = true)
    private void labsMousePressed(int mouseX, int mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (!super.mousePressed(getGui().mc, mouseX, mouseY)) {
            cir.setReturnValue(false);
            return;
        }

        var newMode = switch (button) {
            case 0 -> labs$getAccessible().labs$getMode().next(false);
            case 1 -> labs$getAccessible().labs$getMode().next(true);
            default -> BetterMemoryCardModes.INPUT;
        };
        labs$getAccessible().labs$setMode(newMode);

        setHoverText(labsModeDescriptions.get(newMode));

        labs$setTextureDetails();

        labs$getAccessible().labs$syncMemoryInfo();
        super.playPressSound(getGui().mc.getSoundHandler());
        cir.setReturnValue(true);
    }

    @Unique
    private AccessibleGuiAdvancedMemoryCard labs$getAccessible() {
        return ((AccessibleGuiAdvancedMemoryCard) (Object) getGui());
    }

    @Unique
    private void labs$setTextureDetails() {
        if (LabsBetterMemoryCardModes.LABS_ADDED_MODES.containsKey(labs$getAccessible().labs$getMode())) {
            labs$useLabsTexture = true;
            labs$iconId = LabsBetterMemoryCardModes.LABS_ADDED_MODES.get(labs$getAccessible().labs$getMode());
            labs$texX = -1;
            labs$texY = -1;
            return;
        }
        labs$texX = (labs$getAccessible().labs$getMode().ordinal() + 3) * labs$textureSize;
        labs$texY = 232;
        labs$iconId = -1;
        labs$useLabsTexture = false;
    }
}
