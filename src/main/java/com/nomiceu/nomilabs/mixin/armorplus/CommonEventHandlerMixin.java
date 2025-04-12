package com.nomiceu.nomilabs.mixin.armorplus;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.config.LabsConfig;
import com.sofodev.armorplus.common.events.CommonEventHandler;
import com.sofodev.armorplus.common.registry.items.ItemFragment;

/**
 * Allows disabling of fragment drops.
 */
@Mixin(value = CommonEventHandler.class, remap = false)
public class CommonEventHandlerMixin {

    @Inject(method = "registerMobDrop", at = @At("HEAD"), cancellable = true)
    private static void disableFragmentDrops(LivingDropsEvent event, boolean enableDrop, ItemStack drop,
                                             CallbackInfo ci) {
        if (LabsConfig.modIntegration.disableArmorPlusFragDrops && drop.getItem() instanceof ItemFragment)
            ci.cancel();
    }
}
