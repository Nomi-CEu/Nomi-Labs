package com.nomiceu.nomilabs.mixin.draconicevolution;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.brandon3055.draconicevolution.api.fusioncrafting.IFusionCraftingInventory;
import com.brandon3055.draconicevolution.blocks.tileentity.TileCraftingInjector;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.config.LabsConfig;

/**
 * Allows specifying charging time of Crafting Injectors.
 */
@Mixin(value = TileCraftingInjector.class, remap = false)
public abstract class TileCraftingInjectorMixin {

    @Shadow
    private int chargeSpeedModifier;

    @Shadow
    public abstract int getPedestalTier();

    @Inject(method = "setCraftingInventory", at = @At("RETURN"))
    private void setNewChargingTime(IFusionCraftingInventory craftingInventory, CallbackInfoReturnable<Boolean> cir) {
        var chargingTimes = LabsConfig.modIntegration.draconicEvolutionIntegration.fusionChargingTime;
        var tier = getPedestalTier();

        if (tier >= chargingTimes.length) {
            NomiLabs.LOGGER.error("[TileCraftingInjector] No Fusion Injector Crafting Time for Tier {}!", tier);
            NomiLabs.LOGGER.error("[TileCraftingInjector] Defaulting to {}! Check your Config!", chargeSpeedModifier);
            return;
        }

        chargeSpeedModifier = Math.max(1, chargingTimes[tier]);
    }
}
