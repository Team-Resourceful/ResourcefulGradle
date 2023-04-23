package com.teamresourceful.resourcefulsettings.extensions;

import org.gradle.api.provider.Property;

public interface ResourcefulSettingsExt {

    Property<String> getPatch();
    Property<String> getReleaseType();
    Property<String> getBuild();
}
