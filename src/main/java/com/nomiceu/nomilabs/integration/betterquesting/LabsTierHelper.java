package com.nomiceu.nomilabs.integration.betterquesting;

import betterquesting.api.api.QuestingAPI;
import betterquesting.questing.QuestDatabase;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.util.LabsModeHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

/**
 * Used by Nomi-CEu for Rich Presence.
 * <p>
 * As Quest ID Lists are not long, checking it each time is not expensive.
 * <p>
 * Also, Mod Specific Event Bus Subscribers are annoying.
 */
@SideOnly(Side.CLIENT)
public class LabsTierHelper {
    private static int[] IDS;
    private static String[] SLUGS;
    private static String[] NAMES;

    private static UUID uuid;
    // Using Integer instead of int, so it can be null
    private static Integer cacheID;

    public static void preInit() {
        boolean normal = LabsModeHelper.isNormal();
        if (!normal && !LabsModeHelper.isExpert()) {
            if (LabsConfig.advanced.tierSettings.defaultMode == LabsConfig.Advanced.TierSettings.DefaultModeType.NORMAL) {
                normal = true;
            }
        }
        if (normal) {
            IDS = LabsConfig.advanced.tierSettings.normalModeQuestIds;
            SLUGS = LabsConfig.advanced.tierSettings.normalModeSlugs;
            NAMES = LabsConfig.advanced.tierSettings.normalModeFormatted;
        } else {
            IDS = LabsConfig.advanced.tierSettings.expertModeQuestIds;
            SLUGS = LabsConfig.advanced.tierSettings.expertModeSlugs;
            NAMES = LabsConfig.advanced.tierSettings.expertModeFormatted;
        }
        uuid = null;
        cacheID = null;
    }

    @SuppressWarnings("unused")
    public static String getTierSlug() {
        return getOrDefault(LabsConfig.advanced.tierSettings.defaultSlug, SLUGS);
    }

    @SuppressWarnings("unused")
    public static String getTierName() {
        return getOrDefault(LabsConfig.advanced.tierSettings.defaultFormatted, NAMES);
    }

    private static void rebuildCacheTier() {
        var inputUuid = QuestingAPI.getQuestingUUID(Minecraft.getMinecraft().player);
        uuid = inputUuid;
        if (inputUuid == null) {
            cacheID = null;
            return;
        }
        for (int i = IDS.length - 1; i >= 0; i--) {
            var quest = QuestDatabase.INSTANCE.getValue(IDS[i]);
            if (quest != null && quest.isComplete(uuid)) {
                cacheID = i;
                return;
            }
        }
        cacheID = null;
    }

    private static void resetCacheTier() {
        uuid = null;
        cacheID = null;
    }

    private static String getOrDefault(String defaultValue, String[] list) {
        if (!Loader.isModLoaded(LabsValues.BQU_MODID)) return Minecraft.getMinecraft().player == null ? "" : defaultValue;
        if (Minecraft.getMinecraft().player != null) {
            rebuildCacheTier();
        } else
            resetCacheTier();
        if (cacheID == null) return uuid == null ? "" : defaultValue;
        return list[cacheID];
    }
}
