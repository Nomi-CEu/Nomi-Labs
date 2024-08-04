package com.nomiceu.nomilabs.event;

import net.minecraft.client.resources.Language;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * An event that is called AFTER language is changed and/or resources are refreshed.
 * <p>
 * Called Client Side ONLY.
 */
public class LabsResourcesRefreshedEvent extends Event {

    public final Type refreshType;
    public final Language oldLanguage;
    public final Language newLanguage;

    public LabsResourcesRefreshedEvent() {
        this.oldLanguage = null;
        this.newLanguage = null;
        this.refreshType = Type.RESOURCES_REFRESHED;
    }

    public LabsResourcesRefreshedEvent(Language oldLanguage, Language newLanguage) {
        this.oldLanguage = oldLanguage;
        this.newLanguage = newLanguage;
        this.refreshType = Type.LANGUAGE_CHANGED;
    }

    public enum Type {
        // When the Language is Changed
        LANGUAGE_CHANGED,
        // When Resources are Refreshed (e.g. On Load, Resource Packs Change, F3 + T, etc.)
        RESOURCES_REFRESHED
    }
}
