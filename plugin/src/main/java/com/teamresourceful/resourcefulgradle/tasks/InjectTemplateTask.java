package com.teamresourceful.resourcefulgradle.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;

public abstract class InjectTemplateTask extends DefaultTask {

    @Input
    public abstract MapProperty<String, Object> getInjectedValues();

    @InputFile
    public abstract RegularFileProperty getSource();

    @OutputDirectory
    final Provider<File> getTargetDir() {
        //I have no clue if there's a better way to do this or if it even works atm
        return getProject().getRootProject().provider(() -> getProject().getBuildDir());
    }

    @TaskAction
    public void injectTemplate() {
        getProject().copy(copySpec -> {
            copySpec.from(getSource());
            copySpec.into(getTargetDir().get());
            copySpec.rename(s -> s.replace(".template", ""));
            copySpec.expand(getInjectedValues().get());
        });
    }
}
