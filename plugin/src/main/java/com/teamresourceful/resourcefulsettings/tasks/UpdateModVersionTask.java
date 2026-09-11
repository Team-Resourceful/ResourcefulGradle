package com.teamresourceful.resourcefulsettings.tasks;

import com.teamresourceful.ResourcefulSettingsPlugin;
import com.teamresourceful.resourcefulsettings.versioning.ModVersion;
import com.teamresourceful.resourcefulsettings.versioning.VersionProperties;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;

import java.util.Map;

public abstract class UpdateModVersionTask extends DefaultTask {

    @Input
    @Option(option = "patch", description = "Patch version for the mod")
    public abstract Property<String> getPatch();

    @Input
    @Option(
            option = "releaseType",
            description = "ReleaseType version for the mod (release/alpha/beta)"
    )
    public abstract Property<String> getReleaseType();

    @Input
    @Option(option = "build", description = "Version build for the mod")
    public abstract Property<String> getBuild();

    @OutputFile
    public abstract RegularFileProperty getOutputFile();

    @TaskAction
    public void updateVersion() {
        VersionProperties.update(Map.of(
                "patch", getPatch().get(),
                "releaseType", getReleaseType().get(),
                "build", getBuild().get()
        ));

        ModVersion newVersion = ModVersion.inferredVersion();

        VersionProperties.update("version", newVersion.toString());

        VersionProperties.writeToFile(
                getOutputFile().get().getAsFile().toPath()
        );

        ResourcefulSettingsPlugin.LOGGER.quiet(
                "Mod version has been updated: {}",
                newVersion
        );
    }
}