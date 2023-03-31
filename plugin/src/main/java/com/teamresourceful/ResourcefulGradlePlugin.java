package com.teamresourceful;

import com.teamresourceful.resourcefulgradle.extensions.ResourcefulGradleExt;
import com.teamresourceful.resourcefulgradle.extensions.TemplateExt;
import com.teamresourceful.resourcefulgradle.tasks.InjectTemplateTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;


public final class ResourcefulGradlePlugin implements Plugin<Project> {

    public static final Logger LOGGER = Logging.getLogger(ResourcefulGradlePlugin.class);
    public static final String RESOURCEFUL_GRADLE_EXTENSION = "resourcefulGradle";
    public static final String GET_MOD_VERSION_TASK = "getModVersion";
    public static final String SET_NIGHTLY_VERSION_TASK = "setNightlyVersion";
    public static final String UPDATE_MOD_VERSION_TASK = "updateModVersion";
    public static final String VERSIONING_GROUP = "versioning";
    public static final String INJECT_ALL_TEMPLATES_TASK = "injectAllTemplates";
    public static final String TEMPLATE_INJECTION_GROUP = "template injection";

    public void apply(Project project) {
        ResourcefulGradleExt extension = project.getExtensions().create(RESOURCEFUL_GRADLE_EXTENSION, ResourcefulGradleExt.class);

        extension.getTemplates().all(template -> {
            String name = template.getName().substring(0,1).toUpperCase() + template.getName().substring(1);
            project.getTasks().register("inject" + name, InjectTemplateTask.class, task -> {
                task.setGroup(TEMPLATE_INJECTION_GROUP);
                task.getSource().set(template.getSource());
                task.getInjectedValues().set(template.getInjectedValues());
            });
        });

        project.getTasks().register(INJECT_ALL_TEMPLATES_TASK, InjectTemplateTask.class, task -> {
            task.setGroup(TEMPLATE_INJECTION_GROUP);
            for (TemplateExt template : extension.getTemplates()) {
                task.getSource().set(template.getSource());
                task.getInjectedValues().set(template.getInjectedValues());
                task.injectTemplate();
            }
        });

        //Idk if I should automatically force project versions. will leave here as a reminder to think about it more
        //project.setVersion(GetModVersionTask.getCurrentVersionFromFile(project));
    }
}
