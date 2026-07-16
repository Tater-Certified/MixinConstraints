package com.github.tatercertified.mixinconstraints;

import com.github.tatercertified.mixinconstraints.util.Abstractions;
import com.github.tatercertified.mixinconstraints.util.Version;
import space.vectrix.ignite.Ignite;
import space.vectrix.ignite.mod.ModContainer;

public class IgniteAbstractionsImpl extends Abstractions {

    @Override
    protected boolean isDevEnvironment() {
        // Bukkit/Ignite doesn't really have a way to check this I don't think
        return false;
    }

    @Override
    protected String getModVersion(String modId) {
        return Ignite.mods().container(modId)
                .map(ModContainer::version)
                .orElse(null);
    }

    @Override
    protected boolean isVersionInRange(String version, String minVersion, String maxVersion, boolean minInclusive, boolean maxInclusive) {
        Version v = new Version(version);
        Version min = new Version(minVersion);
        Version max = new Version(maxVersion);

        // Check against min
        int cmpMin = v.compareTo(min);
        if (minInclusive ? cmpMin < 0 : cmpMin <= 0) {
            return false;
        }

        // Check against max
        int cmpMax = v.compareTo(max);
        return maxInclusive ? cmpMax <= 0 : cmpMax < 0;
    }

    @Override
    protected String getPlatformName() {
        return "Ignite";
    }
}