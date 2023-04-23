package com.teamresourceful.resourcefulsettings.tasks;

import com.teamresourceful.ResourcefulSettingsPlugin;
import com.teamresourceful.resourcefulsettings.versioning.ModVersion;
import com.teamresourceful.resourcefulsettings.versioning.VersionProperties;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;

import java.io.File;
import java.util.Map;

public class UpdateModVersionTask extends DefaultTask {

    private String patch;
    private String releaseType;
    private String build;

    @Option(option = "patch", description = "Patch version for the mod")
    public void setPatch(String patch) {
        this.patch = patch;
    }

    @Input
    public String getPatch() {
        return patch;
    }

    @Input
    public String getReleaseType() {
        return releaseType;
    }

    @Option(option = "releaseType", description = "ReleaseType version for the mod (release/alpha/beta)")
    public void setReleaseType(String releaseType) {
        this.releaseType = releaseType;
    }

    @Input
    public String getBuild() {
        return build;
    }

    @Option(option = "build", description = "Version build for the mod")
    public void setBuild(String build) {
        this.build = build;
    }

    @OutputFile
    Provider<File> getOutputFile() {
        return getProject().getRootProject().provider(() -> getProject().getRootProject().getLayout().getProjectDirectory().file("version.properties").getAsFile());
    }

    @TaskAction
    public void updateVersion() {
        VersionProperties.update(Map.of("patch", patch, "releaseType", releaseType, "build", build));
        ModVersion newVersion = ModVersion.inferredVersion();
        VersionProperties.update("version", newVersion.toString());
        VersionProperties.writeToFile(getOutputFile().get().toPath());
        ResourcefulSettingsPlugin.LOGGER.quiet("Mod version has been updated: {}", newVersion);
    }
}