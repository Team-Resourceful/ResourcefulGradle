package com.teamresourceful.resourcefulsettings.tasks;

import com.teamresourceful.resourcefulsettings.versioning.ModVersion;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;

import static com.teamresourceful.ResourcefulGradlePlugin.LOGGER;

public abstract class PrintModVersionTask extends DefaultTask {

    @TaskAction
    String printModVersion() {
        String currentVersion = ModVersion.fromVersionProps().toString();
        LOGGER.quiet("Current Mod Version is: {}", currentVersion);
        return currentVersion;
    }
}
