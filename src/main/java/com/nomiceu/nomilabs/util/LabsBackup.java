package com.nomiceu.nomilabs.util;

import com.nomiceu.nomilabs.NomiLabs;
import net.minecraftforge.fml.common.StartupQuery;
import net.minecraftforge.fml.common.ZipperUtil;

public class LabsBackup {
    public static void createWorldBackup() {
        try {
            NomiLabs.LOGGER.info("Creating world backup...");
            ZipperUtil.backupWorld();
        } catch (Exception e) {
            StartupQuery.abort();
            NomiLabs.LOGGER.error("Error creating backup. Closing...");
            NomiLabs.LOGGER.throwing(e);
        }
    }
}
