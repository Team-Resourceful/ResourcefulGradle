package com.teamresourceful;

import com.teamresourceful.resourcefulsettings.extensions.ResourcefulSettingsExt;
import com.teamresourceful.resourcefulsettings.tasks.GetModVersionTask;
import com.teamresourceful.resourcefulsettings.tasks.SetNightlyVersionTask;
import com.teamresourceful.resourcefulsettings.tasks.UpdateModVersionTask;
import com.teamresourceful.resourcefulsettings.versioning.ModVersion;
import com.teamresourceful.resourcefulsettings.versioning.VersionProperties;
import org.gradle.api.Plugin;
import org.gradle.api.initialization.Settings;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.TaskContainer;
import org.jetbrains.annotations.NotNull;

/**
 * This is probably the dumbest way to do all of this, but it works.
 * I just need it to work for now and then I can work on coming up with
 * a plan to make it better.
 */
public final class ResourcefulSettingsPlugin implements Plugin<Settings> {

    public static final Logger LOGGER = Logging.getLogger(ResourcefulSettingsPlugin.class);
    public static final String GET_MOD_VERSION_TASK = "getModVersion";
    public static final String SET_NIGHTLY_VERSION_TASK = "setNightlyVersion";
    public static final String UPDATE_MOD_VERSION_TASK = "updateModVersion";
    public static final String VERSIONING_GROUP = "versioning";

    @Override
    public void apply(@NotNull Settings settings) {

        ResourcefulSettingsExt extension = settings.getExtensions().create("resourcefulSettings", ResourcefulSettingsExt.class);

        VersionProperties.map(settings.getRootDir());
        if (!VersionProperties.isEmpty()) {
            ModVersion modVersion = ModVersion.fromVersionProps();
            settings.getDependencyResolutionManagement().getVersionCatalogs().create("libs").version("mod-version", modVersion.toString());
            LOGGER.quiet("{}: {}", settings.getRootProject().getName(), modVersion.toString());
        }

        settings.getGradle().rootProject(project -> {
            extension.getPatch().convention("0");
            extension.getReleaseType().convention("");
            extension.getBuild().convention("0");
            TaskContainer taskContainer = project.getTasks();
            taskContainer.register(UPDATE_MOD_VERSION_TASK, UpdateModVersionTask.class, task -> {
                task.setGroup(VERSIONING_GROUP);
                task.setPatch(extension.getPatch().get());
                task.setReleaseType(extension.getReleaseType().get());
                task.setBuild(extension.getBuild().get());
            });
            taskContainer.register(GET_MOD_VERSION_TASK, GetModVersionTask.class, task -> task.setGroup(VERSIONING_GROUP));
            taskContainer.register(SET_NIGHTLY_VERSION_TASK, SetNightlyVersionTask.class, task -> task.setGroup(VERSIONING_GROUP));
        });
    }
}
