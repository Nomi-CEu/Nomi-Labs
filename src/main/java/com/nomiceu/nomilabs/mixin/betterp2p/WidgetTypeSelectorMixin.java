package com.nomiceu.nomilabs.mixin.betterp2p;

import static com.projecturanus.betterp2p.client.gui.widget.WidgetTypeSelectorKt.ICONS_PER_ROW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.LabsTextures;
import com.nomiceu.nomilabs.integration.betterp2p.ExtendedITypeReceiver;
import com.nomiceu.nomilabs.util.LabsTranslate;
import com.projecturanus.betterp2p.client.gui.GuiAdvancedMemoryCard;
import com.projecturanus.betterp2p.client.gui.widget.ITypeReceiver;
import com.projecturanus.betterp2p.client.gui.widget.WidgetTypeSelector;
import com.projecturanus.betterp2p.util.p2p.ClientTunnelInfo;

import gregtech.api.gui.resources.TextureArea;

/**
 * Allows Changing Input/Output P2P.
 */
@Mixin(value = WidgetTypeSelector.class, remap = false)
public class WidgetTypeSelectorMixin {

    @Shadow
    public ITypeReceiver parent;

    @Shadow
    @Final
    @Mutable
    private int height;

    @Shadow
    private int y;

    @Shadow
    private int x;

    @Shadow
    private int hoveredIdx;

    @Shadow
    @Final
    private int width;

    @Unique
    private int labs$origHeight;

    @Unique
    private List<List<String>> labs$translated;

    @Unique
    private TextureArea[] labs$icons;

    @Unique
    private int labs$hovered;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void saveOrigHeightAndTranslate(int x, int y, List<ClientTunnelInfo> p2pTypes, CallbackInfo ci) {
        labs$origHeight = height;

        labs$translated = new ArrayList<>();
        labs$translated.add(Collections.singletonList(LabsTranslate.translate("nomilabs.gui.better_p2p.input")));
        labs$translated.add(Collections.singletonList(LabsTranslate.translate("nomilabs.gui.better_p2p.output")));
        labs$icons = new TextureArea[] {
                LabsTextures.P2P_INPUT_ICON,
                LabsTextures.P2P_OUTPUT_ICON,
        };

        labs$hovered = -1;
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void checkHeight(GuiAdvancedMemoryCard gui, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (parent instanceof ExtendedITypeReceiver)
            height = labs$origHeight + (18 * 2);
    }

    @Inject(method = "mousePressed", at = @At("HEAD"), cancellable = true)
    private void handleInputOutputPressed(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        if (!(parent instanceof ExtendedITypeReceiver extended)) return;

        if (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) {
            if (labs$hovered != -1) {
                extended.labs$acceptIsInput(labs$hovered == 0);
                ci.cancel();
            }
        }
    }

    /**
     * Split across two functions.
     * <p>
     * Must render before text.
     * Text might not exist.
     */
    @Inject(method = "render",
            at = @At(value = "INVOKE",
                     target = "Lcom/projecturanus/betterp2p/client/gui/GuiAdvancedMemoryCard;drawHoveringText(Ljava/util/List;IILnet/minecraft/client/gui/FontRenderer;)V",
                     remap = true),
            remap = false,
            require = 1)
    private void renderInputOutput1(GuiAdvancedMemoryCard gui, int mouseX, int mouseY, float partialTicks,
                                    CallbackInfo ci) {
        labs$draw(gui, mouseX, mouseY);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void renderInputOutput2(GuiAdvancedMemoryCard gui, int mouseX, int mouseY, float partialTicks,
                                    CallbackInfo ci) {
        if (hoveredIdx == -1) labs$draw(gui, mouseX, mouseY);
    }

    /**
     * Shared Logic Function.
     */
    @Unique
    private void labs$draw(GuiAdvancedMemoryCard gui, int mouseX, int mouseY) {
        if (!(parent instanceof ExtendedITypeReceiver)) return;

        labs$hovered = -1;
        int iconPosY = y + 4 + ((labs$this().getP2pTypes().size() / ICONS_PER_ROW) + 2) * 18;

        for (int i = 0; i < 2; i++) {
            int iconPosX = x + 4 + (i * 18);
            boolean iconHover = mouseX > iconPosX && mouseX < iconPosX + 18 && mouseY > iconPosY &&
                    mouseY < iconPosY + 18;

            if (iconHover) {
                GuiScreen.drawRect(iconPosX, iconPosY, iconPosX + 18, iconPosY + 18, 0xFF00FF00);
                labs$hovered = i;
            }

            // Reset Rendering Values
            GlStateManager.enableBlend();
            GlStateManager.enableTexture2D();
            GlStateManager.enableAlpha();
            GlStateManager.color(1f, 1f, 1f, 1f);
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
            labs$icons[i].draw(iconPosX + 1, iconPosY + 1, 18, 18);
        }

        if (labs$hovered != -1) {
            gui.drawHoveringText(labs$translated.get(labs$hovered), mouseX, mouseY, gui.mc.fontRenderer);
        }
    }

    @Unique
    private WidgetTypeSelector labs$this() {
        return (WidgetTypeSelector) (Object) this;
    }
}
