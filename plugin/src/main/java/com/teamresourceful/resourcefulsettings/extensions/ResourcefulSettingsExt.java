package com.teamresourceful.resourcefulsettings.extensions;

import org.gradle.api.provider.Property;

public interface ResourcefulSettingsExt {

    //Property<Boolean> getAutomaticVersioning();
    Property<String> getPatchVersion();
    Property<String> getVersionPostfix();
    Property<String> getPostfixBuild();
}
