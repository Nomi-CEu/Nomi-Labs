package com.nomiceu.nomilabs.event;

import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * An event that is called AFTER resources are refreshed. (Language Change Refreshes Resources)
 * <p>
 * Note that when resources are refreshed during game load, this will not trigger this event.
 * <p>
 * Called Client Side ONLY.
 */
public class LabsResourcesRefreshedEvent extends Event {}
