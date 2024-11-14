package com.nomiceu.nomilabs;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.event.ClientProxy;
import com.nomiceu.nomilabs.event.CommonProxy;
import com.nomiceu.nomilabs.integration.effortlessbuilding.EffortlessEventHandler;
import com.nomiceu.nomilabs.integration.ftbutilities.event.FTBUtilsEventHandler;
import com.nomiceu.nomilabs.remap.datafixer.DataFixerHandler;
import com.nomiceu.nomilabs.util.LabsSide;

@Mod(modid = LabsValues.LABS_MODID,
     version = LabsValues.LABS_VERSION,
     name = LabsValues.LABS_MODNAME,
     acceptedMinecraftVersions = "[1.12.2]",
     dependencies = "required:forge@[14.23.5.2847,);" + "required-after:codechickenlib@[3.2.3,);" +
             "required-after:groovyscript@[1.1.0,);" + "required-after:gregtech@[2.8,);" + "required-after:gcym;" +
             "required-after:jei@[4.15.0,);" + "required-after:theoneprobe;" + "required-after:configanytime;" +
             "after:advancedrocketry;" +
             "after:libvulpes;" + "after:crafttweaker@[4.1.20,);" + "after:appliedenergistics2;" +
             "after:architecturecraft;" + "after:effortlessbuilding;" + "after:betterquesting;" +
             "after:defaultworldgenerator-port;" + "after:deepmoblearning;" + "after:ftbutilities;" +
             "after:topaddons;")
@SuppressWarnings("unused")
public class NomiLabs {

    public static final Logger LOGGER = LogManager.getLogger(LabsValues.LABS_MODID);

    public NomiLabs() {
        FluidRegistry.enableUniversalBucket();
    }

    @EventHandler
    public void onConstruction(FMLConstructionEvent event) {
        CommonProxy.onConstruction();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        if (LabsSide.isClient())
            ClientProxy.earlyPreInit();
        CommonProxy.preInit();
        if (LabsSide.isClient())
            ClientProxy.latePreInit();

        if (Loader.isModLoaded(LabsValues.EFFORTLESS_MODID) &&
                LabsConfig.modIntegration.effortlessBuildingIntegration.enableEffortlessBuildingIntegration)
            MinecraftForge.EVENT_BUS.register(EffortlessEventHandler.class);

        if (Loader.isModLoaded(LabsValues.FTB_UTILS_MODID) &&
                LabsConfig.modIntegration.enableFTBUtilsIntegration)
            MinecraftForge.EVENT_BUS.register(FTBUtilsEventHandler.class);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        CommonProxy.postInit();
        if (LabsSide.isClient())
            ClientProxy.postInit();
    }

    @EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        CommonProxy.loadComplete();
    }

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        DataFixerHandler.close();
    }
}
