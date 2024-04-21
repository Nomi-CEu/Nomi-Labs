package com.nomiceu.nomilabs.fluid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import com.nomiceu.nomilabs.NomiLabs;
import com.nomiceu.nomilabs.config.LabsConfig;
import com.nomiceu.nomilabs.mixin.AccessibleFluidRegistry;

public class FluidRegistryMixinHelper {

    private static List<Pattern> defaultFluidsRegex;
    private static Map<String, String> defaultFluids;

    public static void preInit() {
        defaultFluidsRegex = new ArrayList<>();
        for (var fluid : LabsConfig.advanced.fluidRegistrySettings.defaultFluids) {
            try {
                defaultFluidsRegex.add(Pattern.compile(fluid));
            } catch (PatternSyntaxException e) {
                NomiLabs.LOGGER.error("Bad Syntax for Default Fluid Regex: {}", fluid);
                NomiLabs.LOGGER.throwing(e);
            }
        }
        NomiLabs.LOGGER.info("[Fluid Registry Mixin]: Compiled Default Fluid Regex List: {}", defaultFluidsRegex);
    }

    public static void loadComplete() {
        if (defaultFluidsRegex.isEmpty() && !LabsConfig.advanced.fluidRegistrySettings.logConflictingFluids) return;

        Map<String, List<String>> nameToRlMap = new HashMap<>();
        AccessibleFluidRegistry.getMasterFluidReference().forEach((key, value) -> {
            nameToRlMap.computeIfAbsent(value.getName(), (k) -> new ArrayList<>());
            nameToRlMap.get(value.getName()).add(key);
        });
        defaultFluids = new HashMap<>();
        boolean hasConflicts = false;
        int conflicts = 0;
        int solves = 0;

        /* Validate Conflicts */
        for (var entry : nameToRlMap.entrySet()) {
            if (entry.getValue().size() <= 1) continue;
            if (!hasConflicts && LabsConfig.advanced.fluidRegistrySettings.logConflictingFluids) {
                NomiLabs.LOGGER.info("---------------- CONFLICTING & SOLVED FLUIDS: ----------------");
                NomiLabs.LOGGER.info("==============================================================");
                hasConflicts = true;
            }
            var type = parseConflict(entry);
            if (!hasConflicts) continue;
            switch (type) {
                case CONFLICT -> conflicts++;
                case SOLVED -> solves++;
            }
        }
        if (hasConflicts) {
            NomiLabs.LOGGER.info("===============================================================");
            NomiLabs.LOGGER.info("--------------- END CONFLICTING & SOLVED FLUIDS ---------------");
            NomiLabs.LOGGER.info("Conflicts: {}/{} | Solves: {}/{}",
                    conflicts, conflicts + solves, solves, conflicts + solves);
        }
        NomiLabs.LOGGER.info("[Fluid Registry Mixin] Generated Default Fluid Map: {}", defaultFluids);
    }

    private static ConflictType parseConflict(Map.Entry<String, List<String>> entry) {
        boolean solved = false;
        Pattern ptnUsed = null;
        if (!defaultFluidsRegex.isEmpty()) {
            for (var ptn : defaultFluidsRegex) {
                if (solved) break;
                for (var option : entry.getValue()) {
                    if (!ptn.matcher(option).matches()) continue;
                    solved = true;
                    defaultFluids.put(entry.getKey(), option);
                    ptnUsed = ptn;
                    break;
                }
            }
        }

        /* Log */
        if (!LabsConfig.advanced.fluidRegistrySettings.logConflictingFluids)
            return solved ? ConflictType.SOLVED : ConflictType.CONFLICT;

        Level level = solved ? Level.INFO : Level.ERROR;
        NomiLabs.LOGGER.log(level, "~~~~~~~~~~~~~~~~~");
        NomiLabs.LOGGER.log(level, solved ? "SOLVED:" : "CONFLICT:");
        NomiLabs.LOGGER.log(level, "Fluid: {}", entry.getKey());
        NomiLabs.LOGGER.log(level, "Possible Options: {}", entry.getValue());
        if (solved) {
            NomiLabs.LOGGER.log(level, "Chosen Option: {}", defaultFluids.get(entry.getKey()));
            NomiLabs.LOGGER.log(level, "Used Regex: {} (No. {})", ptnUsed.pattern(),
                    defaultFluidsRegex.indexOf(ptnUsed) + 1);
        }
        NomiLabs.LOGGER.log(level, "~~~~~~~~~~~~~~~~~");
        return solved ? ConflictType.SOLVED : ConflictType.CONFLICT;
    }

    public enum ConflictType {
        SOLVED,
        CONFLICT
    }

    @Nullable
    public static Map<String, String> getDefaultFluids() {
        return defaultFluids;
    }
}
