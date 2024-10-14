package com.nomiceu.nomilabs.mixin.gregtech;

import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nomiceu.nomilabs.gregtech.mixinhelper.AccessibleSteamBoiler;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.metatileentities.steam.boiler.SteamBoiler;

/**
 * Allows Accessing Steam Boiler Info, and Fixes Continuous Running.
 */
@Mixin(value = SteamBoiler.class, remap = false)
public abstract class SteamBoilerMixin extends MetaTileEntity implements AccessibleSteamBoiler {

    @Shadow
    private int currentTemperature;

    @Shadow
    private int fuelMaxBurnTime;

    @Shadow
    protected abstract int getCooldownInterval();

    @Shadow
    private int timeBeforeCoolingDown;

    @Shadow
    private boolean wasBurningAndNeedsUpdate;

    @Shadow
    private int fuelBurnTimeLeft;

    @Shadow
    public abstract int getMaxTemperate();

    /**
     * Mandatory Ignored Constructor
     */
    public SteamBoilerMixin(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId);
    }

    @Inject(method = "updateCurrentTemperature", at = @At("RETURN"))
    private void checkForNegativeBurnTime(CallbackInfo ci) {
        if (fuelMaxBurnTime > 0 && getOffsetTimer() % 12 == 0) {
            if (fuelBurnTimeLeft < 0) {
                fuelMaxBurnTime = 0;
                timeBeforeCoolingDown = getCooldownInterval();
                // boiler has no fuel now, so queue burning state update
                wasBurningAndNeedsUpdate = true;
            } else if (fuelBurnTimeLeft % 2 != 0 && currentTemperature < getMaxTemperate())
                currentTemperature++;
        }
    }

    @Override
    @Unique
    public int labs$getCurrentTemperature() {
        return currentTemperature;
    }
}
