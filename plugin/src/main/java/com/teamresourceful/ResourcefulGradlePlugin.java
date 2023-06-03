package com.teamresourceful;

import com.teamresourceful.resourcefulgradle.extensions.ResourcefulGradleExt;
import com.teamresourceful.resourcefulgradle.tasks.InjectTemplateTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.TaskProvider;

import java.util.ArrayList;
import java.util.List;


public final class ResourcefulGradlePlugin implements Plugin<Project> {

    public static final Logger LOGGER = Logging.getLogger(ResourcefulGradlePlugin.class);
    public static final String RESOURCEFUL_GRADLE_EXTENSION = "resourcefulGradle";
    public static final String INJECT_ALL_TEMPLATES_TASK = "injectAllTemplates";
    public static final String TEMPLATE_INJECTION_GROUP = "template injection";

    public void apply(Project project) {
        ResourcefulGradleExt extension = project.getExtensions().create(RESOURCEFUL_GRADLE_EXTENSION, ResourcefulGradleExt.class);
        List<TaskProvider<InjectTemplateTask>> injectTasks = new ArrayList<>();

        extension.getTemplates().all(template -> {
            template.getTarget().convention("");
            template.getEscapeBackslash().convention(false);
            String name = template.getName().substring(0,1).toUpperCase() + template.getName().substring(1);
            TaskProvider<InjectTemplateTask> injectTemplateTask = project.getTasks().register("inject" + name, InjectTemplateTask.class, task -> {
                task.setGroup(TEMPLATE_INJECTION_GROUP);
                task.getSource().set(template.getSource());
                task.getTarget().set(template.getTarget());
                task.getEscapeBackslash().set(template.getEscapeBackslash());
                task.getInjectedValues().set(template.getInjectedValues());
            });
            injectTasks.add(injectTemplateTask);
        });

        project.getTasks().register(INJECT_ALL_TEMPLATES_TASK, Task.class, task -> {
            task.setGroup(TEMPLATE_INJECTION_GROUP);
            task.setFinalizedBy(injectTasks);
        });

        //Idk if I should automatically force project versions. will leave here as a reminder to think about it more
        //project.setVersion(GetModVersionTask.getCurrentVersionFromFile(project));
    }
}
