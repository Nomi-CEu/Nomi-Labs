package com.nomiceu.nomilabs.event;

import net.minecraft.client.resources.Language;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * An event that is called AFTER language is changed, and resources are refreshed.
 * <p>
 * Called Client Side ONLY.
 */
public class LabsLanguageChangedEvent extends Event {

    public final Language oldLanguage;
    public final Language newLanguage;

    public LabsLanguageChangedEvent(Language oldLanguage, Language newLanguage) {
        this.oldLanguage = oldLanguage;
        this.newLanguage = newLanguage;
    }
}
