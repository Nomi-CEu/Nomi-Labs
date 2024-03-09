package com.nomiceu.nomilabs.mixin.draconicevolution;

import com.brandon3055.draconicevolution.items.ToolUpgrade;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.brandon3055.draconicevolution.items.ToolUpgrade.*;

@Mixin(value = ToolUpgrade.class, remap = false)
public class ToolUpgradeMixin {
    @Inject(method = "registerUpgrade", at = @At("HEAD"), cancellable = true)
    private static void registerUpgradeExceptChaotic(int id, String upgrade, int maxLevel, CallbackInfo ci) {
        maxLevel = Math.min(maxLevel, 3);
        ID_TO_NAME.put(id, upgrade);
        NAME_TO_ID.put(upgrade, id);
        NAME_MAX_LEVEL.put(upgrade, maxLevel);
        ci.cancel();
    }
}
