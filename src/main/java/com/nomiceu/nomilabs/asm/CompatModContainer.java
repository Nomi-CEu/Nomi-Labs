/*
package com.nomiceu.nomilabs.asm;

import com.google.common.eventbus.EventBus;
import com.nomiceu.nomilabs.LabsValues;
import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.Tags;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.relauncher.FMLInjectionData;

import java.io.File;

public class CompatModContainer extends DummyModContainer {
    private final File source;
    public CompatModContainer() {
        super(new ModMetadata());
        getMetadata().modId = LabsValues.CONTENTTWEAKER_MODID;
        getMetadata().name = NomiLabs.MODNAME;
        getMetadata().version = Tags.VERSION;
        this.source = (File) FMLInjectionData.data()[6];
    }

    @Override
    public File getSource() {
        return source;
    }

    @Override
    public boolean isImmutable()
    {
        return true;
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        return true;
    }
}
*/
