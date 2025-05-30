package com.nomiceu.nomilabs.integration.top;

import static mcjty.theoneprobe.api.TextStyleClass.MODNAME;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import appeng.helpers.ICustomNameObject;
import appeng.integration.modules.theoneprobe.part.PartAccessor;
import appeng.tile.AEBaseTile;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.config.Config;

public class AECustomNameOverride implements IBlockDisplayOverride {

    private static final PartAccessor accessor = new PartAccessor();

    @Override
    public boolean overrideStandardInfo(ProbeMode mode, IProbeInfo info, EntityPlayer player, World world,
                                        IBlockState state, IProbeHitData data) {
        var tile = world.getTileEntity(data.getPos());
        var part = accessor.getMaybePart(tile, data);

        String customName = null;

        // noinspection SimplifyOptionalCallChains
        if (!(tile instanceof AEBaseTile) && !part.isPresent()) return false;

        if (part.isPresent()) {
            if (part.get() instanceof ICustomNameObject nameObj) {
                if (nameObj.hasCustomInventoryName())
                    customName = nameObj.getCustomInventoryName();
            }
        } else if (tile instanceof AEBaseTile base) {
            if (base.hasCustomInventoryName())
                customName = base.getCustomInventoryName();
        }

        if (customName == null) return false;

        customName = customName.trim();
        if (customName.isEmpty()) return false;

        ItemStack pickBlock = data.getPickBlock();
        IElement customNameElement = new CustomNameElement(pickBlock, customName);

        if (Tools.show(mode, Config.getRealConfig().getShowModName()))
            info.horizontal()
                    .item(pickBlock)
                    .vertical()
                    .element(customNameElement)
                    .text(MODNAME + Tools.getModName(state.getBlock()));
        else
            info.horizontal(info.defaultLayoutStyle()
                    .alignment(ElementAlignment.ALIGN_CENTER))
                    .item(pickBlock)
                    .element(customNameElement);

        return true;
    }
}
