package com.moulberry.mixinconstraints;

import com.moulberry.mixinconstraints.util.Abstractions;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.spongepowered.api.Sponge;

public class SpongeAbstractionsImpl extends Abstractions {
    @Override
    protected boolean isDevEnvironment() {
        // Sponge doesn't really have a way to tell this I don't think
        return false;
    }

    @Override
    protected String getModVersion(String modId) {
        return Sponge.pluginManager().plugin(modId)
                .map(container -> container.metadata()
                        .version()
                        .getQualifier())
                .orElse(null);
    }

    @Override
    protected boolean isVersionInRange(String version, String minVersion, String maxVersion) {
        ArtifactVersion currentVersion = new DefaultArtifactVersion(version);
        ArtifactVersion min = new DefaultArtifactVersion(minVersion);
        ArtifactVersion max = new DefaultArtifactVersion(maxVersion);

        if(min.compareTo(max) > 0) {
            throw new IllegalArgumentException("minVersion (" + minVersion + ") is greater than maxVersion (" + maxVersion + ")");
        }

        return currentVersion.compareTo(min) >= 0 && currentVersion.compareTo(max) <= 0;
    }

    @Override
    public String getPlatformName() {
        return "Sponge";
    }
}
