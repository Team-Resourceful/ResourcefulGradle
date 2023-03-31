package com.teamresourceful.resourcefulgradle.extensions;

import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.tasks.Nested;

public interface ResourcefulGradleExt {
    //Not sure if Set is better than Container or not. It doesn't seem to prevent duplicate names though
    @Nested
    NamedDomainObjectContainer<TemplateExt> getTemplates();

    default void templates(Action<? super NamedDomainObjectContainer<TemplateExt>> action) {
        action.execute(getTemplates());
    }

}
