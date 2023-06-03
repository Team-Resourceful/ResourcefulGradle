package com.teamresourceful.resourcefulgradle.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.*;
import org.gradle.api.tasks.options.Option;

import java.io.File;

public abstract class InjectTemplateTask extends DefaultTask {

    @Input
    public abstract MapProperty<String, Object> getInjectedValues();

    @InputFile
    public abstract RegularFileProperty getSource();

    @Option(option = "target", description = "outputs to an arbitrary file name")
    @Input
    public abstract Property<String> getTarget();

    @OutputDirectory
    final Provider<File> getTargetDir() {
        //I have no clue if there's a better way to do this
        return getProject().getRootProject().provider(() -> getProject().getBuildDir());
    }

    @Option(option = "escapeBackslash", description = "Set to true to disable escape sequences conversion")
    @Optional
    @Input
    public abstract Property<Boolean> getEscapeBackslash();

    @TaskAction
    public void injectTemplate() {
        getProject().copy(copySpec -> {
            copySpec.from(getSource());
            copySpec.into(getTargetDir().get());
            copySpec.rename(s -> {
                String target = getTarget().get();
                return target.isEmpty() ? s.replace(".template", "") : target;
            });
            copySpec.expand(getInjectedValues().get(), expandDetails -> expandDetails.getEscapeBackslash().set(getEscapeBackslash()));
        });
    }
}
