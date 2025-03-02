package com.nomiceu.nomilabs.mixin.vanilla;

import java.util.List;
import java.util.Map;

import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.resources.Language;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.mixinhelper.AccessibleGuiLanguage;

/**
 * Contracts the List Section, and Stops Language Refresh on Element Clicked.
 */
@Mixin(targets = "net.minecraft.client.gui.GuiLanguage$List")
public abstract class GuiLanguageListMixin {

    @Shadow(aliases = "this$0")
    @Final
    GuiLanguage this$0;

    @Shadow
    @Final
    private List<String> langCodeList;

    @Shadow
    @Final
    private Map<String, Language> languageMap;

    @ModifyArg(method = "<init>(Lnet/minecraft/client/gui/GuiLanguage;Lnet/minecraft/client/Minecraft;)V",
               at = @At(value = "INVOKE",
                        target = "net/minecraft/client/gui/GuiSlot.<init>(Lnet/minecraft/client/Minecraft;IIIII)V"),
               index = 3)
    private static int getNewArgs(int top) {
        if (LabsConfig.advanced.languageModifyOption == LabsConfig.Advanced.LanguageModifyOption.NONE) return top;
        return top * 2;
    }

    @Inject(method = "elementClicked(IZII)V", at = @At("HEAD"), cancellable = true)
    protected void cancelElementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY, CallbackInfo ci) {
        if (LabsConfig.advanced.languageModifyOption == LabsConfig.Advanced.LanguageModifyOption.NONE) return;
        Language language = languageMap.get(langCodeList.get(slotIndex));
        ((AccessibleGuiLanguage) this$0).labs$getLanguageManager().setCurrentLanguage(language);
        ci.cancel();
    }
}
