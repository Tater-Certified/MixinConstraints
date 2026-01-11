package com.moulberry.mixinconstraints;

import com.google.gson.Gson;
import com.moulberry.mixinconstraints.util.Abstractions;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SpongeAbstractionsImpl extends Abstractions {
    private static final Gson GSON = new Gson();
    private static final HashMap<String, String> PLUGINS = getModVersions();

    @Override
    protected boolean isDevEnvironment() {
        // Sponge doesn't really have a way to tell this I don't think
        return false;
    }

    @Override
    protected String getModVersion(String modId) {
        return PLUGINS.get(modId);
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

    private static HashMap<String, String> getModVersions() {
        HashMap<String, String> map = new HashMap<>();
        Path workingDir = Paths.get("");

        Path pluginsDir = workingDir.resolve("mods/plugins");

        if (!Files.isDirectory(pluginsDir)) {
            System.err.println("Plugins directory not found: " + pluginsDir);
            return map;
        }

        try (DirectoryStream<Path> jars = Files.newDirectoryStream(pluginsDir, "*.jar")) {
            for (Path jarPath : jars) {
                String[] info = readPluginInfo(jarPath);
                if (info != null) {
                    map.put(info[0], info[1]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    private static String[] readPluginInfo(Path jarPath) {
        try (JarFile jar = new JarFile(jarPath.toFile())) {
            JarEntry entry = jar.getJarEntry("sponge_plugins.json");
            if (entry == null) {
                return null;
            }

            try (Reader reader = new InputStreamReader(
                    jar.getInputStream(entry), StandardCharsets.UTF_8)) {

                SpongePluginsJson json =
                        GSON.fromJson(reader, SpongePluginsJson.class);

                if (json.plugins != null) {
                    for (SpongePluginsJson.Plugin plugin : json.plugins) {
                        return new String[] {plugin.id, plugin.version};
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed reading " + jarPath.getFileName() + ": " + e.getMessage());
        }
        return null;
    }

    static class SpongePluginsJson {
        List<Plugin> plugins;

        static class Plugin {
            String id;
            String version;
        }
    }
}
