package com.nomiceu.nomilabs.mixinhelper;

public class ResourcesObserver {

    private static boolean shouldCallEvent = false;

    public static void onLoadComplete() {
        shouldCallEvent = true;
    }

    public static boolean shouldCallEvent() {
        return shouldCallEvent;
    }
}
