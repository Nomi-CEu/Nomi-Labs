package com.nomiceu.nomilabs.mixin.deepmoblearning;

import static mustapelto.deepmoblearning.DMLConstants.Gui.SimulationChamber.DATA_MODEL_SLOT;

import java.awt.*;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;

import com.google.common.collect.ImmutableList;
import com.nomiceu.nomilabs.integration.deepmobevolution.AccessibleGuiMachine;
import com.nomiceu.nomilabs.integration.deepmobevolution.JEIExcluded;

import mustapelto.deepmoblearning.client.gui.GuiMachine;
import mustapelto.deepmoblearning.client.gui.GuiSimulationChamber;
import mustapelto.deepmoblearning.common.tiles.TileEntityMachine;
import mustapelto.deepmoblearning.common.util.Point;

/**
 * Adds JEI Exclusion Areas to Gui Simulation Chamber.
 */
@Mixin(value = GuiSimulationChamber.class, remap = false)
public abstract class GuiSimulationChamberMixin extends GuiMachine implements JEIExcluded {

    /**
     * Default Ignored Constructor
     */
    private GuiSimulationChamberMixin(TileEntityMachine tileEntity, EntityPlayer player, World world, int width,
                                      int height, Point redstoneModeButtonLocation) {
        super(tileEntity, player, world, width, height, redstoneModeButtonLocation);
    }

    @Override
    public List<Rectangle> getGuiExclusionAreas() {
        return ImmutableList.of(
                ((AccessibleGuiMachine) this).getRedstoneButtonRect(),
                new Rectangle(
                        guiLeft + DATA_MODEL_SLOT.LEFT,
                        guiTop + DATA_MODEL_SLOT.TOP,
                        DATA_MODEL_SLOT.WIDTH,
                        DATA_MODEL_SLOT.HEIGHT));
    }
}
