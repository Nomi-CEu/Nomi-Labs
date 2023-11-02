package com.nomiceu.nomilabs;

import com.nomiceu.nomilabs.event.ClientProxy;
import com.nomiceu.nomilabs.event.CommonProxy;
import com.nomiceu.nomilabs.item.registry.LabsItems;
import com.nomiceu.nomilabs.recipe.HandFramingRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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
                + "after:appliedenergistics2;"
                + "after:jei@[4.15.0,);"
                + "after:theoneprobe;")
@SuppressWarnings("unused")
public class NomiLabs {
    public static final Logger LOGGER = LogManager.getLogger(LabsValues.LABS_MODID);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        ClientProxy.preInit();
        CommonProxy.preInit();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        CommonProxy.postInit();
    }

    @EventHandler
    // load "Do your mod setup. Build whatever data structures you care about." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
    }

    @EventHandler
    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
    }

    static {
        FluidRegistry.enableUniversalBucket();
    }
}
