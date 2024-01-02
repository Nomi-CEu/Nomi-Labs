package com.nomiceu.nomilabs;

import com.nomiceu.nomilabs.event.ClientProxy;
import com.nomiceu.nomilabs.event.CommonProxy;
import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = LabsValues.LABS_MODID,
        version = LabsValues.LABS_VERSION,
        name = LabsValues.LABS_MODNAME,
        acceptedMinecraftVersions = "[1.12.2]",
        dependencies = "required:forge@[14.23.5.2847,);"
                + "required-after:codechickenlib@[3.2.3,);"
                + "required-after:groovyscript@[0.6.0,);"
                + "required-after:gregtech;"
                + "required-after:packmode;"
                + "after:appliedenergistics2;"
                + "after:jei@[4.15.0,);"
                + "after:theoneprobe;")
@SuppressWarnings("unused")
public class NomiLabs {
    public static final Logger LOGGER = LogManager.getLogger(LabsValues.LABS_MODID);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            ClientProxy.preInit();
        CommonProxy.preInit();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        CommonProxy.postInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        CommonProxy.init();
    }

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        DataFixerHandler.close();
    }

    static {
        FluidRegistry.enableUniversalBucket();
    }
}
