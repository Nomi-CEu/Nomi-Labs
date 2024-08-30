package com.nomiceu.nomilabs.mixin.vanilla;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;

import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.mixinhelper.AccessibleGuiLanguage;
import com.nomiceu.nomilabs.mixinhelper.GuiCustomConfirmOpenLink;
import com.nomiceu.nomilabs.mixinhelper.GuiLanguageShouldReloadJEI;

/**
 * Adds Nomi-CEu & Nomi-Labs Translation Pack 'Advertisements' to Language Selection Screen.
 */
@Mixin(GuiLanguage.class)
public abstract class GuiLanguageMixin extends GuiScreen implements AccessibleGuiLanguage {

    @Unique
    private static final int DOWNLOAD_PACK_BTN_ID = 10;

    @Unique
    private static final int SHOW_GH_BTN_ID = 11;

    @Unique
    private static final String TRANSLATIONS_GH = "https://github.com/Nomi-CEu/Nomi-CEu-Translations";

    @Unique
    private static final String TRANSLATIONS_DOWNLOAD = "https://nightly.link/Nomi-CEu/Nomi-CEu-Translations/workflows/pushbuildpack/main?preview";

    @Unique
    private String downloadPackNote;

    @Unique
    private Language previousLang;

    @Shadow
    private GuiOptionButton forceUnicodeFontBtn;

    @Shadow
    private GuiOptionButton confirmSettingsBtn;

    @Shadow
    @Final
    private GameSettings game_settings_3;

    @Shadow
    @Final
    private LanguageManager languageManager;

    @Shadow
    protected GuiScreen parentScreen;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void savePreviousLang(GuiScreen screen, GameSettings gameSettingsObj, LanguageManager manager,
                                 CallbackInfo ci) {
        previousLang = manager.getCurrentLanguage();
    }

    @Inject(method = "initGui", at = @At("TAIL"))
    public void moveAndAddElements(CallbackInfo ci) {
        if (LabsConfig.advanced.languageModifyOption == LabsConfig.Advanced.LanguageModifyOption.NONE) return;
        forceUnicodeFontBtn.y = height - 28;
        confirmSettingsBtn.y = height - 28;
        confirmSettingsBtn.displayString = I18n.format("nomilabs.gui.language.exit");
        addButton(new GuiOptionButton(DOWNLOAD_PACK_BTN_ID, width / 2 - 155 + 160, height - 56,
                I18n.format("nomilabs.gui.language.download")));
        addButton(new GuiOptionButton(SHOW_GH_BTN_ID, width / 2 - 155, height - 56,
                I18n.format("nomilabs.gui.language.gh")));
        downloadPackNote = LabsConfig.advanced.languageModifyOption == LabsConfig.Advanced.LanguageModifyOption.LABS ?
                I18n.format("nomilabs.gui.language.download.note.labs") :
                I18n.format("nomilabs.gui.language.download.note.nomi");
    }

    @Inject(method = "drawScreen",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/client/gui/GuiLanguage$List;drawScreen(IIF)V",
                     shift = At.Shift.AFTER),
            cancellable = true)
    public void drawNewLanguageScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (LabsConfig.advanced.languageModifyOption == LabsConfig.Advanced.LanguageModifyOption.NONE) return;
        drawCenteredString(fontRenderer, TextFormatting.BOLD + I18n.format("options.language"), width / 2, 10,
                0xffffff);
        drawCenteredString(fontRenderer,
                I18n.format(LabsConfig.advanced.languageModifyOption == LabsConfig.Advanced.LanguageModifyOption.LABS ?
                        "nomilabs.gui.language_pack_labs" : "nomilabs.gui.language_pack_nomi"),
                width / 2, 10 + (int) (fontRenderer.FONT_HEIGHT * 2.5), 0xffffff);
        drawCenteredString(fontRenderer, "(" + I18n.format("options.languageWarning") + ")", width / 2,
                10 + fontRenderer.FONT_HEIGHT * 4, 0x808080);
        super.drawScreen(mouseX, mouseY, partialTicks);
        ci.cancel();
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    public void handleCustomActions(GuiButton button, CallbackInfo ci) {
        if (LabsConfig.advanced.languageModifyOption == LabsConfig.Advanced.LanguageModifyOption.NONE) return;
        switch (button.id) {
            case 6: // Done Button
                var language = languageManager.getCurrentLanguage();
                var code = language.getLanguageCode();
                if (code.equals(previousLang.getLanguageCode())) break;

                game_settings_3.language = code;
                FMLClientHandler.instance().refreshResources(VanillaResourceType.LANGUAGES);
                fontRenderer
                        .setUnicodeFlag(languageManager.isCurrentLocaleUnicode() || game_settings_3.forceUnicodeFont);
                fontRenderer.setBidiFlag(languageManager.isCurrentLanguageBidirectional());
                game_settings_3.saveOptions();

                mc.displayGuiScreen(new GuiLanguageShouldReloadJEI(parentScreen));
                ci.cancel();
                break;
            case DOWNLOAD_PACK_BTN_ID:
                mc.displayGuiScreen(new GuiCustomConfirmOpenLink(this, TRANSLATIONS_DOWNLOAD, downloadPackNote,
                        DOWNLOAD_PACK_BTN_ID));
                ci.cancel();
                break;
            case SHOW_GH_BTN_ID:
                mc.displayGuiScreen(new GuiCustomConfirmOpenLink(this, TRANSLATIONS_GH, null, SHOW_GH_BTN_ID));
                ci.cancel();
                break;
        }
    }

    @Override
    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    @Override
    public void confirmClicked(boolean result, int id) {
        if (LabsConfig.advanced.languageModifyOption == LabsConfig.Advanced.LanguageModifyOption.NONE) return;
        String url = switch (id) {
            case DOWNLOAD_PACK_BTN_ID -> TRANSLATIONS_DOWNLOAD;
            case SHOW_GH_BTN_ID -> TRANSLATIONS_GH;
            default -> "";
        };

        if (result) {
            try {
                Class<?> desktopClass = Class.forName("java.awt.Desktop");
                Object desktopObj = desktopClass.getMethod("getDesktop").invoke(null);
                desktopClass.getMethod("browse", URI.class).invoke(desktopObj, new URI(url));
            } catch (URISyntaxException | ClassNotFoundException | InvocationTargetException | IllegalAccessException |
                     NoSuchMethodException e) {
                NomiLabs.LOGGER.error("Failed to Open Link!");
                NomiLabs.LOGGER.throwing(e);
            }
        }
        this.mc.displayGuiScreen(this);
    }
}
