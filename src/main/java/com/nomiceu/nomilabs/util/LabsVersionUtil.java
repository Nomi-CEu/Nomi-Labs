package com.nomiceu.nomilabs.util;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.google.common.base.CharMatcher;

public class LabsVersionUtil {

    public static LabsVersion getVersionFromString(String version) {
        var splitVersion = version.split("\\.");

        List<Integer> versionParts = new ArrayList<>();
        for (String part : splitVersion) {
            String extracted = CharMatcher.digit().retainFrom(part);
            if (extracted.isEmpty()) continue;

            versionParts.add(Integer.parseInt(extracted));
        }
        return new LabsVersion(versionParts);
    }

    /**
     * Labs Version object, used for comparing versions of mods.
     * <p>
     * Note that all text in the version is ignored!
     */
    public static class LabsVersion implements Comparable<LabsVersion> {

        private final List<Integer> versionParts;

        public LabsVersion(List<Integer> versionParts) {
            this.versionParts = versionParts;
        }

        @Override
        public int compareTo(@NotNull LabsVersion o) {
            if (versionParts.isEmpty() || o.versionParts.isEmpty()) return 0;

            int result = 0;
            for (int i = 0; i < Math.min(versionParts.size(), o.versionParts.size()); i++) {
                result = Integer.compare(versionParts.get(i), o.versionParts.get(i));
                if (result != 0) break;
            }
            if (result == 0) {
                result = Integer.compare(versionParts.size(), o.versionParts.size());
            }

            return result;
        }
    }
}
