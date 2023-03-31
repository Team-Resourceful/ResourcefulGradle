package com.teamresourceful.resourcefulsettings.versioning;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.teamresourceful.ResourcefulGradlePlugin.LOGGER;

public class VersionProperties {

    private VersionProperties() {
        //throw new UtilityClassError();
    }

    private static final Map<String, String> VALUES = new HashMap<>();

    public static Map<String, String> values() {
        return Collections.unmodifiableMap(VALUES);
    }

    public static boolean isEmpty() {
        return VALUES.isEmpty();
    }

    public static String get(String property) {
        return VALUES.get(property);
    }

    public static void update(String property, String value) {
        VALUES.merge(property, value, (s, s2) -> s2);
    }

    public static void update(Map<String, String> properties) {
        properties.forEach((s, s2) -> VALUES.merge(s, s2, (s1, s21) -> s21));
    }

    public static void map(File rootDir) {
        VersionProperties.map(getPath(rootDir));
    }

    public static void map(Path versionPropsPath) {
        try {
            final List<String> versionProps = Files.readAllLines(versionPropsPath);
            VALUES.clear();
            LOGGER.debug("version props:");
            //TODO add handling for missing values in version.props
            versionProps.forEach(s -> {
                String[] kv = s.split("=");
                VALUES.putIfAbsent(kv[0], kv[1]);
                LOGGER.debug(s);
            });

        } catch (IOException e) {
            LOGGER.error("version.properties file missing in root project directory!", e);
        }
    }

    public static void writeToFile(File rootDir) {
        VersionProperties.writeToFile(getPath(rootDir));
    }

    public static void writeToFile(Path versionPropsPath) {
        final List<String> lines = new ArrayList<>();
        VALUES.forEach((s, s2) -> lines.add(s + "=" + s2));

        try {
            Files.write(versionPropsPath, lines);
        }  catch (IOException e) {
            LOGGER.error("Could not write to version.properties in root project directory!", e);
        }
    }

    private static Path getPath(File rootDir) {
        return Path.of(rootDir + "/version.properties");
    }
}
