package com.nomiceu.nomilabs.integration.effortlessbuilding;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

import com.google.common.collect.ImmutableMap;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.util.LabsTranslate;

import nl.requios.effortlessbuilding.BuildConfig;
import nl.requios.effortlessbuilding.EffortlessBuilding;
import nl.requios.effortlessbuilding.buildmodifier.ModifierSettingsManager;
import scala.Int;

public class GenericReachUpgrade {

    public static final int CREATIVE_LEVEL = -1;
    public static Map<Integer, ReachInfo> REACH_MAP = ImmutableMap.of(
            -1, new ReachInfo(() -> BuildConfig.reach.maxReachCreative,
                    () -> LabsConfig.modIntegration.effortlessBuildingIntegration.axisReachCreative,
                    () -> LabsConfig.modIntegration.effortlessBuildingIntegration.blocksPlacedCreative),
            0, new ReachInfo(() -> BuildConfig.reach.maxReachLevel0,
                    () -> LabsConfig.modIntegration.effortlessBuildingIntegration.axisReach0,
                    () -> LabsConfig.modIntegration.effortlessBuildingIntegration.blocksPlaced0),
            1, new ReachInfo(() -> BuildConfig.reach.maxReachLevel1,
                    () -> LabsConfig.modIntegration.effortlessBuildingIntegration.axisReach1,
                    () -> LabsConfig.modIntegration.effortlessBuildingIntegration.blocksPlaced1),
            2, new ReachInfo(() -> BuildConfig.reach.maxReachLevel2,
                    () -> LabsConfig.modIntegration.effortlessBuildingIntegration.axisReach2,
                    () -> LabsConfig.modIntegration.effortlessBuildingIntegration.blocksPlaced2),
            3, new ReachInfo(() -> BuildConfig.reach.maxReachLevel3,
                    () -> LabsConfig.modIntegration.effortlessBuildingIntegration.axisReach3,
                    () -> LabsConfig.modIntegration.effortlessBuildingIntegration.blocksPlaced3));

    // A complete overwrite so everything can be localized!
    public static ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand,
                                                           int upgradeLevel) {
        if (player.isCreative()) {
            if (world.isRemote) {
                EffortlessBuilding.log(player,
                        LabsTranslate.translate("effortlessbuilding.use.reach_upgrade.creative.1"));
                EffortlessBuilding.log(player,
                        LabsTranslate.translate("effortlessbuilding.use.reach_upgrade.creative.2"));
            }
            return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
        }

        ModifierSettingsManager.ModifierSettings modifierSettings = ModifierSettingsManager.getModifierSettings(player);
        int currentLevel = modifierSettings.getReachUpgrade();
        if (currentLevel < upgradeLevel - 1 && currentLevel >= 0) {
            if (world.isRemote) EffortlessBuilding.log(player,
                    LabsTranslate.translate("effortlessbuilding.use.reach_upgrade.previous", upgradeLevel - 1));
        }
        if (currentLevel == upgradeLevel - 1) {
            ReachInfo currentReach = REACH_MAP.get(upgradeLevel - 1);
            ReachInfo newReach = REACH_MAP.get(upgradeLevel);
            modifierSettings.setReachUpgrade(upgradeLevel);

            if (world.isRemote) {
                EffortlessBuilding.log(player,
                        LabsTranslate.translate("effortlessbuilding.use.reach_upgrade.success.1"));
                EffortlessBuilding.log(player, LabsTranslate.translate("effortlessbuilding.use.reach_upgrade.success.2",
                        currentReach.getAxis(), newReach.getAxis()));
                EffortlessBuilding.log(player, LabsTranslate.translate("effortlessbuilding.use.reach_upgrade.success.3",
                        currentReach.getDistance(), newReach.getDistance()));
            }
            player.setHeldItem(hand, ItemStack.EMPTY);

            SoundEvent soundEvent = new SoundEvent(new ResourceLocation("entity.player.levelup"));
            player.playSound(soundEvent, 1f, 1f);

            return new ActionResult<>(EnumActionResult.PASS, ItemStack.EMPTY);
        } else if (currentLevel > upgradeLevel - 1) {
            if (world.isRemote) EffortlessBuilding.log(player,
                    LabsTranslate.translate("effortlessbuilding.use.reach_upgrade.already_used"));
        }
        return new ActionResult<>(EnumActionResult.FAIL, player.getHeldItem(hand));
    }

    public static void addInformation(List<String> tooltip, int level) {
        ReachInfo reach = REACH_MAP.get(level);
        tooltip.add(LabsTranslate.translate("effortlessbuilding.item.reach_upgrade.tooltip.1"));
        tooltip.add(LabsTranslate.translate("effortlessbuilding.item.reach_upgrade.tooltip.2", reach.getAxis()));
        tooltip.add(LabsTranslate.translate("effortlessbuilding.item.reach_upgrade.tooltip.3", reach.getDistance()));
    }

    public static class ReachInfo {
        // Uses Suppliers so that we can change configs on the fly
        private final Supplier<Integer> distance;
        private final Supplier<Integer> axis;
        private final Supplier<Integer> maxBlocks;

        public ReachInfo(Supplier<Integer> distance, Supplier<Integer> axis, Supplier<Integer> maxBlocks) {
            this.distance = distance;
            this.axis = axis;
            this.maxBlocks = maxBlocks;
        }

        public int getDistance() {
            return distance.get();
        }

        public int getAxis() {
            return axis.get();
        }

        public int getMaxBlocks() {
            return maxBlocks.get();
        }
    }
}
