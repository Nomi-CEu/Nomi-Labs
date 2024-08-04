package com.nomiceu.nomilabs.mixinhelper;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.groovyscript.registry.ReloadableRegistryManager;
import com.nomiceu.nomilabs.event.LabsResourcesRefreshedEvent;
import com.nomiceu.nomilabs.util.LabsTranslate;

@SideOnly(Side.CLIENT)
public class GuiLanguageShouldReloadJEI extends GuiYesNo {

    protected final GuiScreen parent;
    protected final LabsResourcesRefreshedEvent event;
    protected final List<String> descLines = new ArrayList<>();

    /**
     * @param parent The parent screen of the GuiLanguage instance that is creating this GUI.
     * @param event  Event to fire
     */
    public GuiLanguageShouldReloadJEI(GuiScreen parent, LabsResourcesRefreshedEvent event) {
        // Parent Callback will never happen, just insert default id 0
        super(parent, LabsTranslate.translate("nomilabs.gui.language_jei.title"), "", 0);
        this.event = event;
        this.parent = parent;
    }

    @Override
    public void initGui() {
        super.initGui();
        descLines.clear();
        addToList("nomilabs.gui.language_jei.desc.1");
        addToList("nomilabs.gui.language_jei.desc.2");
    }

    private void addToList(String key) {
        descLines.addAll(fontRenderer.listFormattedStringToWidth(LabsTranslate.translate(key), width - 50));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        int i = 90;

        for (String s : descLines) {
            drawCenteredString(fontRenderer, s, width / 2, i, 0xFFFFFF);
            i += fontRenderer.FONT_HEIGHT;
        }
    }

    @Override
    protected void actionPerformed(@NotNull GuiButton button) {
        if (button.id == 0) {
            // Reload JEI to refresh description text
            // noinspection UnstableApiUsage
            ReloadableRegistryManager.reloadJei(false);
        }

        // Fire Lang Change Event (AFTER JEI RELOAD)
        MinecraftForge.EVENT_BUS.post(event);

        mc.displayGuiScreen(parent);
    }
}
