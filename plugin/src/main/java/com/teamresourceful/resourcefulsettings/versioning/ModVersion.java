package com.teamresourceful.resourcefulsettings.versioning;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.teamresourceful.ResourcefulSettingsPlugin.LOGGER;

public record ModVersion(int major, int minor, int patch, String releaseType, int build, long buildTime) {

    private static final Pattern VERSION_PATTERN = Pattern.compile("^(\\d+)\\.(\\d+)\\.(\\d+)(?i:-(alpha|beta)(?:\\.(\\d+))?(?:\\+(\\d+))?)?$");
    private static final String DEFAULT_VERSION = "0.0.0";

    public static ModVersion of(int major, int minor, int patch) {
        return new ModVersion(major, minor, patch, "", 0, 0);
    }

    public static ModVersion of(int major, int minor, int patch, String releaseType) {
        return new ModVersion(major, minor, patch, releaseType, 0, 0);
    }

    public static ModVersion of(int major, int minor, int patch, String releaseType, int build) {
        return new ModVersion(major, minor, patch, releaseType, build, 0);
    }

    public static ModVersion of(int major, int minor, int patch, String releaseType, int build, long buildTime) {
        return new ModVersion(major, minor, patch, releaseType, build, buildTime);
    }

    public static ModVersion fromVersionProps() {
        Matcher matcher = VERSION_PATTERN.matcher(VersionProperties.get("version"));
        if (matcher.find()) {
            LOGGER.debug("Matcher found version: {}", matcher);
            LOGGER.debug("Matcher group count: {}", matcher.groupCount());
            return getModVersion(matcher);
        }
        LOGGER.error("version.properties has an invalid version format!");
        return ModVersion.of(0,0,0);
    }

    public static ModVersion inferredVersion() {
        Map<String, String> versionProps = VersionProperties.values();
        String initialMCVersion = versionProps.getOrDefault("initialMCVersion", DEFAULT_VERSION);
        String currentMCVersion = versionProps.getOrDefault("currentMCVersion", DEFAULT_VERSION);
        String patch = versionProps.getOrDefault("patch", "0");
        String releaseType = versionProps.getOrDefault("releaseType", "");
        String build = versionProps.getOrDefault("build", "");

        if (initialMCVersion.equals(DEFAULT_VERSION) || currentMCVersion.equals(DEFAULT_VERSION)) {
            throw new IllegalArgumentException("Initial and Current MC versions must be specified!");
            //may be a better error to throw but this will do for now
        }

        //tokenize initial and current mc versions
        List<Integer> init = tokenizeVersionString(initialMCVersion);
        List<Integer> cur = tokenizeVersionString(currentMCVersion);

        //determine mod major and minor
        int major = cur.get(1) - init.get(1) + 1;
        int minor = Objects.equals(cur.get(1), init.get(1)) ? cur.get(2) - init.get(2) : cur.get(2);

        return ModVersion.of(major, minor, Integer.parseInt(patch), sanitizeReleaseType(releaseType), Integer.parseInt(build));
    }

    @NotNull
    private static ModVersion getModVersion(@NotNull Matcher matcher) {
        int major = matcherGroupToInt(matcher, 1);
        int minor = matcherGroupToInt(matcher, 2);
        int patch = matcherGroupToInt(matcher, 3);
        String releaseType = matcher.group(4) != null ? matcher.group(4) : "";
        int build = matcher.group(5) != null ? matcherGroupToInt(matcher, 5) : 0;
        int buildTime = matcher.group(6) != null ? matcherGroupToInt(matcher, 6) : 0;

        return ModVersion.of(major, minor, patch, sanitizeReleaseType(releaseType), build, buildTime);
    }

    @NotNull
    private static String sanitizeReleaseType(String releaseType) {
        return releaseType.equals("release") ? "" : releaseType;
    }

    private static int matcherGroupToInt(@NotNull Matcher matcher, int group) {
        return Integer.parseInt(matcher.group(group));
    }

    private static List<Integer> tokenizeVersionString(String string) {
        List<String> version = Arrays.asList(string.split("\\."));
        List<Integer> ret = new ArrayList<>(version.stream().mapToInt(Integer::parseInt).boxed().toList());
        if (ret.size() == 2) ret.add(0);
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(major)
                .append(".")
                .append(minor)
                .append(".")
                .append(patch);

        if (releaseType != null && !releaseType.isEmpty()) {
            builder.append("-").append(releaseType);
            if (build > 0) {
                builder.append(".").append(build);
            }
        }
        if (buildTime > 0) {
            builder.append("+").append(buildTime);
        }
        return builder.toString();
    }
}
