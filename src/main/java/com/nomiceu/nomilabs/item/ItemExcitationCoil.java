package com.nomiceu.nomilabs.item;

import com.nomiceu.nomilabs.tooltip.LabsTooltipHelper;
import gregtech.client.utils.TooltipHelper;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static com.nomiceu.nomilabs.util.LabsTranslate.translate;
import static com.nomiceu.nomilabs.util.LabsTranslate.translateFormat;

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

    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        EntityEquipmentSlot slot = event.getSlot();
        if (event.getFrom().isItemEqual(event.getTo()) || slot != EntityEquipmentSlot.HEAD)
            return;

        ItemStack from = event.getFrom();
        ItemStack to = event.getTo();
        if (!to.isEmpty() && to.getItem() instanceof ItemExcitationCoil) {
            event.getEntityLiving().addPotionEffect(
                    new PotionEffect(MobEffects.NIGHT_VISION, 999999, 0, true, false));
        }
        if (!from.isEmpty() && from.getItem() instanceof ItemExcitationCoil) {
            event.getEntityLiving().removePotionEffect(MobEffects.NIGHT_VISION);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        tooltip.add(translate("tooltip.nomilabs.excitationcoil.description"));
        if (LabsTooltipHelper.isShiftDown()) {
            tooltip.add(translate("tooltip.nomilabs.excitationcoil.placeable"));
            tooltip.add(translate("tooltip.nomilabs.excitationcoil.wearable"));
            tooltip.add(translateFormat("tooltip.nomilabs.excitationcoil.night_vision", TooltipHelper.RAINBOW_SLOW));
        } else
            tooltip.add(translateFormat("tooltip.nomilabs.general.press_shift_for_usages", TooltipHelper.BLINKING_ORANGE));
    }
}
