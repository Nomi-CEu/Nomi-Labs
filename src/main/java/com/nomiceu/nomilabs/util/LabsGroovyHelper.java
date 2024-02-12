package com.nomiceu.nomilabs.util;

import com.cleanroommc.groovyscript.GroovyScript;

public class LabsGroovyHelper {
    public static boolean isRunningGroovyScripts() {
        return GroovyScript.getSandbox().isRunning();
    }
}
