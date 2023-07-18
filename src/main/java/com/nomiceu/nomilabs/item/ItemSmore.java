package com.nomiceu.nomilabs.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ItemSmore extends ItemFood {
    List<PotionEffect> potionEffects;
    private IRarity rarity = null;

    public ItemSmore(int healAmount, float saturation, ResourceLocation rl, CreativeTabs creativeTab) {
        super(healAmount, saturation, true);
        setAlwaysEdible();
        setCreativeTab(creativeTab);
        setRegistryName(rl);
        potionEffects = new ArrayList<>();
    }

    public void addPotionEffect(Potion potion, int potionDuration, int amplifier) {
        if (potion == null)
            return;

        potionEffects.add(new PotionEffect(potion, potionDuration, amplifier));
    }

    public ItemSmore setRarity(IRarity rarity) {
        this.rarity = rarity;
        return this;
    }

    @Override
    protected void onFoodEaten(@NotNull ItemStack stack, World worldIn, @NotNull EntityPlayer player)
    {
        if (!worldIn.isRemote && this.potionEffects != null)
        {
            for (PotionEffect potionAdd : potionEffects) {
                if (potionAdd != null)
                    player.addPotionEffect(new PotionEffect(potionAdd));
            }
        }
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return this.rarity == null ? super.getForgeRarity(stack) : this.rarity;
    }
}