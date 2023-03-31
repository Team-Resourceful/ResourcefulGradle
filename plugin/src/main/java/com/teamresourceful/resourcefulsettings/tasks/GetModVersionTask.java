package com.teamresourceful.resourcefulsettings.tasks;

import com.teamresourceful.resourcefulsettings.versioning.ModVersion;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;

import static com.teamresourceful.ResourcefulGradlePlugin.LOGGER;

public abstract class GetModVersionTask extends DefaultTask {

    @Internal
    @TaskAction
    String getModVersion() {
        String currentVersion = ModVersion.fromVersionProps().toString();
        LOGGER.quiet("Current Mod Version is: {}", currentVersion);
        return currentVersion;
    }
}
