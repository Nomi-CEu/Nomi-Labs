package com.nomiceu.nomilabs.item;

import com.nomiceu.nomilabs.block.registry.LabsBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        super.onUpdate(stack, world, entity, itemSlot, isSelected);
        if (!(entity instanceof EntityPlayer player))
            return;
        if (!player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem().equals((LabsBlocks.ITEMS.get(LabsBlocks.EXCITATION_COIL))) && !world.isRemote)
            player.removePotionEffect(MobEffects.NIGHT_VISION);
    }
}
