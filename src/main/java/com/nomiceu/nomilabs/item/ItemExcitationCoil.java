package com.nomiceu.nomilabs.item;

import com.nomiceu.nomilabs.block.registry.LabsBlocks;
import com.nomiceu.nomilabs.tooltip.LabsTooltipHelper;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import gregtech.client.utils.TooltipHelper;

import java.util.List;
import java.util.Objects;

public class ItemExcitationCoil extends ItemBlock {

    public ItemExcitationCoil(Block block) {
        super(block);
        setRegistryName(Objects.requireNonNull(block.getRegistryName()));
        setCreativeTab(block.getCreativeTab());
        setMaxStackSize(64);
    }

    @Nullable
    @Override
    public EntityEquipmentSlot getEquipmentSlot(@NotNull ItemStack stack) {
        return EntityEquipmentSlot.HEAD;
    }

    @Override
    public void onArmorTick(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack itemStack) {
        if (!world.isRemote)
            player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 999999, 0, true, false));
    }

    @Override
    public void onUpdate(@NotNull ItemStack stack, @NotNull World world, @NotNull Entity entity, int itemSlot, boolean isSelected) {
        if (!(entity instanceof EntityPlayer player))
            return;
        if (!player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem().equals((LabsBlocks.ITEMS.get(LabsBlocks.EXCITATION_COIL))) && !world.isRemote)
            player.removePotionEffect(MobEffects.NIGHT_VISION);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.nomilabs.excitationcoil.description_item"));
        if (LabsTooltipHelper.isShiftDown()) {
            tooltip.add(TextFormatting.GRAY+ I18n.format("tooltip.nomilabs.excitationcoil.placeable"));
            tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.nomilabs.excitationcoil.wearable"));
            tooltip.add(TooltipHelper.RAINBOW_SLOW + I18n.format("tooltip.nomilabs.excitationcoil.night_vision"));
        } else
            tooltip.add(TooltipHelper.BLINKING_ORANGE + I18n.format("tooltip.nomilabs.general.press_shift_for_usages"));
    }
}
