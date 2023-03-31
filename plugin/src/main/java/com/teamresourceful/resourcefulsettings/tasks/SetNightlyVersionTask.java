package com.teamresourceful.resourcefulsettings.tasks;

import com.teamresourceful.resourcefulsettings.versioning.ModVersion;
import com.teamresourceful.resourcefulsettings.versioning.VersionProperties;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

import java.io.File;

import static com.teamresourceful.ResourcefulGradlePlugin.LOGGER;

public abstract class SetNightlyVersionTask extends DefaultTask {

    @OutputFile
    Provider<File> getOutputFile() {
        //I have no clue if there's a better way to do this
        return getProject().getRootProject().provider(() -> getProject().getRootProject().getLayout().getProjectDirectory().file("version.properties").getAsFile());
    }

    @TaskAction
    void setNightlyVersion() {
        var nightly = String.format("%s+%s", ModVersion.fromVersionProps(), (int) (System.currentTimeMillis() / 1000));
        VersionProperties.update("version", nightly);
        LOGGER.quiet("Nightly version has been set: {}", nightly);
        VersionProperties.writeToFile(getOutputFile().get().toPath());
    }
}
