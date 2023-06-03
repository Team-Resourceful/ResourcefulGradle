package com.teamresourceful.resourcefulgradle.extensions;

import org.gradle.api.Named;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;

public interface TemplateExt extends Named {
    MapProperty<String, Object> getInjectedValues();
    RegularFileProperty getSource();
    Property<String> getTarget();
    Property<Boolean> getEscapeBackslash();
}
