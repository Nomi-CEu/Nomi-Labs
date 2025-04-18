package com.nomiceu.nomilabs.mixin.ae2;

import net.minecraft.inventory.Container;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import com.nomiceu.nomilabs.integration.ae2.AccessibleGuiMEMonitorable;

import appeng.api.util.IConfigManager;
import appeng.api.util.IConfigurableObject;
import appeng.client.gui.AEBaseMEGui;
import appeng.client.gui.implementations.GuiMEMonitorable;
import appeng.container.implementations.ContainerMEMonitorable;
import appeng.util.IConfigManagerHost;

/**
 * Part of the implementation of
 * <a href="https://github.com/AE2-UEL/Applied-Energistics-2/commit/f1e5a33a6e0f391c3083eb0ef163a3adaea7577f">AE2
 * f1e5a33</a> for v0.56.5.
 */
@Mixin(value = GuiMEMonitorable.class, remap = false)
public abstract class GuiMEMonitorableMixin extends AEBaseMEGui implements AccessibleGuiMEMonitorable {

    @Shadow
    @Final
    @Mutable
    private IConfigManager configSrc;

    @Shadow
    @Final
    @Mutable
    private ContainerMEMonitorable monitorableContainer;

    /**
     * Mandatory Ignored Constructor
     */
    private GuiMEMonitorableMixin(Container container) {
        super(container);
    }

    @Override
    public void labs$updateContainer(ContainerMEMonitorable newCont) {
        inventorySlots = newCont;

        configSrc = ((IConfigurableObject) inventorySlots).getConfigManager();
        (monitorableContainer = (ContainerMEMonitorable) inventorySlots).setGui((IConfigManagerHost) this);
    }
}
