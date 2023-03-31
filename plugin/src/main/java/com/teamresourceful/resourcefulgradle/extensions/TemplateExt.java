package com.teamresourceful.resourcefulgradle.extensions;

import org.gradle.api.Named;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.MapProperty;

public interface TemplateExt extends Named {
    MapProperty<String, Object> getInjectedValues();
    RegularFileProperty getSource();
}
