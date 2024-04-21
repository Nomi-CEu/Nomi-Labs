package com.nomiceu.nomilabs.util;

import com.cleanroommc.groovyscript.GroovyScript;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.nomiceu.nomilabs.NomiLabs;

public class LabsGroovyHelper {

    public static boolean isRunningGroovyScripts() {
        return GroovyScript.getSandbox().isRunning();
    }

    public static void throwOrGroovyLog(Exception e) {
        if (LabsGroovyHelper.isRunningGroovyScripts()) {
            GroovyLog.get().exception(e);
        } else {
            NomiLabs.LOGGER.throwing(e);
        }
    }
}
